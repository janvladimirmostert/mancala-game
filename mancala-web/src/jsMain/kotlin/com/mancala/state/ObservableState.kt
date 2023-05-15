package com.mancala.state

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@JsExport
@ExperimentalJsExport
@Suppress("NON_EXPORTABLE_TYPE")
open class ObservableState(
	@JsName("change")
	var change: ((field: String, value: Any?) -> Unit)? = null,
) {

	private val observers = mutableListOf<(field: String, value: Any?) -> Unit>()
	private var parent: ObservableState? = null
	private val children = mutableListOf<ObservableState>()

	interface ObservableValue<T> {
		fun clear()
	}

	fun notifyParents(property: KProperty<*>, value: Any?) {
		this.parent?.change?.invoke(property.name, value)
		this.parent?.observers?.onEach { it(property.name, value) }
		this.parent?.notifyParents(property = property, value = value)
	}

	fun notifyChildren(property: KProperty<*>, value: Any?) {
		this.children.forEach { child ->
			child.change?.invoke(property.name, value)
			child.observers.onEach { observer ->
				observer(property.name, value)
			}
			child.notifyChildren(property, value)
		}
	}

	@JsName("watching_state")
	fun <T : ObservableState, P : ObservableState> watching(
		state: T,
	): ReadOnlyProperty<P, T> {
		return object : ReadOnlyProperty<P, T> {

			init {
				state.parent = this@ObservableState
				this@ObservableState.children.add(state)
			}

			private var fieldValue: T = state

			override fun getValue(thisRef: P, property: KProperty<*>): T {
				return this.fieldValue
			}
		}
	}

	@JsName("watching_variable")
	fun <T : Any?> watching(
		initialValue: T,
		also: ((ObservableValue<T>) -> Unit)? = null,
		setValue: ((value: T) -> Unit)? = null,
	): ReadWriteProperty<Any, T> {
		return object : ReadWriteProperty<Any, T>, ObservableValue<T> {

			init {
				also?.invoke(this)
			}

			private var fieldValue: T = initialValue
			private var fieldName: String = "?"

			override fun getValue(thisRef: Any, property: KProperty<*>): T {
				fieldName = property.name
				return this.fieldValue
			}

			override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
				fieldName = property.name
				if (this.fieldValue != value) {
					this.fieldValue = value
					this@ObservableState.change?.invoke(property.name, value)
					this@ObservableState.observers.onEach { it(fieldName, initialValue) }
					this@ObservableState.notifyParents(property, value)
					this@ObservableState.notifyChildren(property, value)
					setValue?.invoke(value)
				}
			}

			override fun clear() {
				if (this.fieldValue != initialValue) {
					this.fieldValue = initialValue
					this@ObservableState.change?.invoke(fieldName, initialValue)
					this@ObservableState.observers.onEach { it(fieldName, initialValue) }
					setValue?.invoke(initialValue)
				}
			}

		}
	}

	@JsName("subscribe")
	fun subscribe(handler: (field: String, value: Any?) -> Unit) {
		observers.add(handler)
	}

	@JsName("unsubscribe")
	fun unsubscribe(handler: (field: String, value: Any?) -> Unit) {
		observers.remove(handler)
	}

}



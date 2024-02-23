package reactredux.reducers

import kotlinx.browser.localStorage
import reactredux.actions.AddTodo
import reactredux.actions.DeleteTodo
import reactredux.actions.EditTodo
import reactredux.actions.ToggleTodo
import reactredux.entities.Todo
import redux.RAction

fun saveState(state: Array<Todo>) {
    val serializedState = JSON.stringify(state)
    localStorage.setItem("todos", serializedState)
}

fun todos(state: Array<Todo>, action: RAction): Array<Todo> {
    val newState = when (action) {
        is AddTodo -> {
            val updatedState = state + Todo(action.id, action.text, false)
            saveState(updatedState)
            updatedState
        }
        is ToggleTodo -> {
            val updatedState = state.map {
                if (it.id == action.id) {
                    it.copy(completed = !it.completed)
                } else {
                    it
                }
            }.toTypedArray()
            saveState(updatedState)
            updatedState
        }
        is DeleteTodo -> {
            val updatedState = state.filterNot { it.id == action.id }.toTypedArray()
            saveState(updatedState)
            updatedState
        }
        is EditTodo -> {
            val updatedState = state.map {
                if (it.id == action.id) {
                    it.copy(text = action.newText)
                } else {
                    it
                }
            }.toTypedArray()
            saveState(updatedState)
            updatedState
        }
        else -> state
    }

    return newState
}

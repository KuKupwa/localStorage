package reactredux.reducers

import kotlinx.browser.localStorage
import reactredux.entities.Todo
import reactredux.enums.VisibilityFilter
import redux.RAction

data class State(
    val todos: Array<Todo> = loadTodosFromLocalStorage(),
    val visibilityFilter: VisibilityFilter = VisibilityFilter.SHOW_ALL
)

fun loadTodosFromLocalStorage(): Array<Todo> {
    val serializedTodos = localStorage.getItem("todos")
    return if (serializedTodos != null) {
        try {
            JSON.parse<Array<JsonTodo>>(serializedTodos).map { jsonTodo ->
                Todo(
                    id = jsonTodo.id,
                    text = jsonTodo.text,
                    completed = jsonTodo.completed
                )
            }.toTypedArray()
        } catch (e: Throwable) {
            console.error("Ошибка парсинга из local-storage:", e.message)
            emptyArray()
        }
    } else {
        emptyArray()
    }
}

data class JsonTodo(
    val id: Int,
    val text: String,
    val completed: Boolean
)

fun rootReducer(
    state: State,
    action: Any
) = State(
    todos(state.todos, action.unsafeCast<RAction>()),
    visibilityFilter(state.visibilityFilter, action.unsafeCast<RAction>())
)

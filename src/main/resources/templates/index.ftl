<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Todo List</title>
</head>
<body>
    <h1>Todo List</h1>
    <form action="/add" method="post">
        <input type="text" name="title" placeholder="Add new todo" required />
        <button type="submit">Add</button>
    </form>
    <ul>
        <#list todos as todo>
            <li>
                ${todo.title}
                <#if !todo.completed>
                    <form action="/complete/${todo.id}" method="post">
                        <button type="submit">Complete</button>
                    </form>
                <#else>
                    <span>(Completed)</span>
                </#if>
                <form action="/delete/${todo.id}" method="post">
                    <button type="submit">Delete</button>
                </form>
            </li>
        </#list>
    </ul>
</body>
</html>
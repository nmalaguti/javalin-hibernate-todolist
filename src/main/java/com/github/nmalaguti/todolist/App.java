package com.github.nmalaguti.todolist;

import com.github.nmalaguti.todolist.config.JavalinConfig;
import com.github.nmalaguti.todolist.models.Todo;
import com.github.nmalaguti.todolist.repositories.TodoRepository;

import static io.javalin.rendering.template.TemplateUtil.model;

public class App {
    public static void main( String[] args ) {
        var app = JavalinConfig.createApp();
        app.start(8080);

        var repository = new TodoRepository();

        app.get("/", ctx -> {
            ctx.render("index.ftl", model("todos", repository.getAllTodos()));
        });

        app.post("/add", ctx -> {
            var title = ctx.formParamAsClass("title", String.class)
                    .check(t -> !t.trim().isEmpty(), "Title cannot be empty")
                    .get();

            var todo = new Todo(title, false);
            repository.save(todo);
            ctx.redirect("/");
        });

        app.post("/complete/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class).get();
            repository.markAsCompleted(id);
            ctx.redirect("/");
        });

        app.post("/delete/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class).get();
            repository.delete(id);
            ctx.redirect("/");
        });
    }
}

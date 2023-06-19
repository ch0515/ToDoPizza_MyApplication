package com.example.todopizza_myapplication;

public class Note {
    int _id;
    String todo;
    String topping;

    public Note(int _id, String todo, String topping){
        this._id = _id;
        this.todo = todo;
        this.topping = topping;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getTopping(){return topping;}

    public void setTopping(String topping){this.topping = topping;}
}

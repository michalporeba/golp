# Game of Life (Patterns) 

This project is based on [Allen Holub](https://en.wikipedia.org/wiki/Allen_Holub)'s [Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) implementation presented in his book "[Holub on Patterns](https://g.co/kgs/BMJcPP)" and on [his website](https://www.holub.com/software/life/index.html).

It is not just a copy. After reading his book and the original source code I'm attempting to follow the book to reimplement the program as closely as I can while using Java 15 (instead of 1.8) and JavaFX (rather than Swing). I am doing this to better understand Java, JavaFX and OOD patterns. 


## Key Points

The `GameOfLife` class is the entry point which is responsible for bootsrapping the JavaFX application and wiring it all up.
Only the basic stage setup is done in there as most of the UI is created by the business objects. 

It might be somewhat controversial, especially if you come (as it was my case) from the .net or (Microsoft) approach to object oriented programming. 
But I'm trying to follow Alen Holub's approach, where objects are responsible for their UI creation. 

So once we have the menu bar added to the stage, we allow the `MainMenu` singleton instance to attache to it. 

```java
MenuBar menuBar = new MenuBar();
MainMenu.getInstance().attachTo(menuBar);
```

It introduces a tight coupling between `MainMenu` class and the `JavaFx` library. However, it is not a problem,
as the `MainMenu` represents the UI element only, and invokes functions on the domain model. 
  
The game mechanics are implemented by the `Universe` class.


 
## Patterns
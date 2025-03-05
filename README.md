AP Project, AI Agent (Recipe Recommender Agent) <br>
Instructor: Dr. Hamidreza Hosseinkhani <br>
Kimia Vanaei, Student Number: 401107613 <br>
Sharif University Of Technology, Computer Engineering Department
----------------------------------------------------------

This project has been implemented in WSL (IntelliJ IDEA, Maven project), and making great use of Docker. You have to install mysql, neo4j and ollama images in docker. (You may check their corresponding codes in the project to ensure the addresses are compatible with yours.) <br>
To make the user experience better and be able to start the agent by simply typing "yum" in WSL terminal, follow these steps:

1. Create a file in ~/bin named "yum" with this content:    #!/bin/bash mvn -q clean compile exec:java -Dexec.mainClass="Main" <br>
2. Add the following content to the end of ~/.bashrc file:    export PATH="$HOME/bin:$PATH" <br>

- For the first method of recipe finding, i used some of MealDB APIs. Website link:    https://www.themealdb.com/api.php <br>
- For neo4j search, i added the following tables to my mysql database in docker: (recipes, ingredients, tags, measurements, website, recipe_tag, chef, media). All the tables are taken from this database: (https://github.com/gadsone/sql6). Then connect localhost to neo4j browser, and add all the nodes and their relationships to the neo4j knowledge graph. Please make sure you read the stated website and also the neo4j search code in project codes, before creating graph.  <br>

![graph](https://github.com/user-attachments/assets/1427f100-5a88-4589-8a20-9ff6896b491f)

- For SQL search, i used 5k-recipes.db in this repo: (https://github.com/josephrmartinez/recipe-dataset). You have to convert the file to .sql format to be able to add it to mysql databases in docker.

This project has been designed in a user-friendly manner to enhance user experience , including simple way of running the agent, colored outputs, and featuring intuitive menus for easy navigation. It also provides the user with an option to save the recipes in a .txt file for later use.

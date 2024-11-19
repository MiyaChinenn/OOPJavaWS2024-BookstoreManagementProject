To run source code:

1.	Please ensure that you open the project in Intellij ULTIMATE, you can obtain this version of Intellij by applying for a student/teacher licence OR by getting the 30-day trial licence. Visit this site for registration and download: Free Educational Licenses - Community Support 
2.	Ensure you have JDK 21 or higher. But I recommend JDK 21 since this is version the project was developed in.
3.	Please ensure that you have MySql server installed on your machine and is running. Create a database on MySql named ‘bookstore’ (or anything of your choice, but you have to make a slight modification to the source code). Import the database included in this source folder, ‘bookstoredatabase.sql’. Go the database.java file, change the connection string according to your machine configuration.
![image](https://github.com/user-attachments/assets/ad0a270c-19f8-4e9a-a45f-40d72339b83f)
4.	Go to File -> Project Structure -> Modules -> Dependencies -> Add (‘+’ symbol) -> add JARs or Directories. Then locate under the project folder scr -> lib. Add the JAR files in this folder. 
5.	Run the source code from BookStoreManagementApplication.java. This is where the ‘main’ method locates.

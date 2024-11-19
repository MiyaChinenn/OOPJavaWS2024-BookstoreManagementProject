Module 61CSE215: JAVA OOP - WS2024  
Supervised by Dr. Tran Hong Ngoc  

Group Members:
Vũ Mạnh Nghĩa - 10422053  
Lương Tiểu Cương - 10422089  
Nguyễn Thiên Nguyên - 10422059  
Nguyễn Hoàng Yến Ngọc - 10422056  

Final submission includes:
1. Full report
2. Source code (code + database)
3. Demo
4. Guideline for using plugins or addins or new components.
5. Slide (pdf and pptx)  
  
Submission:
1. Full report is submitted on the ILIAS website.
2. Source code (code + database) is above, please download and set up following the intrucstion on the Part 4 to set up the environment, and watch the video demo on Part 3 to know more how our system works.
3. Demo:
![video](https://drive.google.com/file/d/1fWaM7kwMU0deziHt-8a0Co3AjXMxPAu1/view?usp=sharing)
4. Guideline for using plugins or addins or new components:  

To run source code:  
- Please ensure that you open the project in Intellij ULTIMATE, you can obtain this version of Intellij by applying for a student/teacher licence OR by getting the 30-day trial licence. Visit this site for registration and download:  
[Free Educational Licenses - Community Support](https://www.jetbrains.com/community/education/#students).  
- Ensure you have JDK 21 or higher. But I recommend JDK 21 since this is version the project was developed in.
- Please ensure that you have MySql server installed on your machine and is running. Create a database on MySql named ‘bookstore’ (or anything of your choice, but you have to make a slight modification to the source code). Import the database included in this source folder, ‘bookstoredatabase.sql’. Go the database.java file, change the connection string according to your machine configuration.  
 ![image](https://github.com/user-attachments/assets/52273141-db3e-437a-a9d0-8e092d758276)
- Go to File -> Project Structure -> Modules -> Dependencies -> Add (‘+’ symbol) -> add JARs or Directories. Then locate under the project folder scr -> lib. Add the JAR files in this folder. 
- Run the source code from BookStoreManagementApplication.java. This is where the ‘main’ method locates.


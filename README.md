# Recipe Website Java App


![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/9465916c-ea9b-4905-bafc-905cdac20ab1)


The application has the basic functionalities of a recipe website. It supports the following operations:

For all users:
- List all recipes: `/recipes/all`
- List recipes with meat: `/recipes/withMeat`
- List vegetarian recipes: `/recipes/vegetarian`
- List vegan recipes: `/recipes/vegan`
- View single recipe details (by id): `/recipes/details/{id}`
- Search for recipes by different parameters (each parameter is optional): `/recipes/search`

For not authenticated users:
- Register by filling a register form and verifying the profile by a verification token sent
  on the email given in the form: `/users/register`
- Requesting a new mail with verification token in case the first one was deleted (by username). This
  request is allowed only for users with not verified profiles:
  `/users/register/sendNewVerificationMail?username=`
- Reset forgotten password via secure token send on email: `/password/reset`
- Login by username and password allowed only for verified profiles: `/users/login`

For authenticated users:
- Add recipe: `/recipes/add`
- Update recipe (by id) allowed only for the owner of the recipe and the admin: `/recipes/edit/{id}`
- Delete recipe (by id) allowed only for the owner of the recipe and the admin: `/recipes/delete/{id}`
- Upload picture for a recipe (by recipe id) allowed for all authenticated users. Post request:
  `/recipes/details/{id}/picture/add`
- Delete picture (by recipe id and picture id) allowed for the owner of the picture and the admin:
  `/recipes/details/{recipeId}/picture/delete?pictureId=`
- Each user can add or remove a recipe from his list of favorite recipes (by recipe id, POST request to rest API):
  `/api/recipes/{id}/addOrRemoveFromFavorites`
- View posted comments for a recipe (wih path variable recipe id) by GET request to:
  `/api/{recipeId}/comments`
- Post comments for a recipe (with path variable recipeId) by POST request to:`/api/{recipeId}/comments`
  (the request accepts request parameter a valid  CommentDto)
- Delete a comment (by recipeId and commentId) allowed only for the owner of the comment and the admin:
  `/api/{recipeId}/comments/{commentId}`
- View profile details allowed only for the owner of the profile (by userId): `"/users/profile/{id}`
- Edit profile details allowed only for the owner of the profile (by userId): `/users/profile/{id}/editProfile`
- List all recipes uploaded by a user, allowed only for the user (by userId): `/users/profile/{id}/addedRecipes`
- List all pictures uploaded by a user, allowed only for the user (by userId): `/users/profile/{id}/addedPictures`
- List users favotite recipes, allowed only for the user (by userId): `/users/profile/{id}/favoriteRecipes`
- Delete a picture added by user, allowed only for the user (by userId and pictureId):
  `/users/{id}/deletePicture?pictureId=`

For admins:
- View number of registered users, all uploaded recipes, number of vegan, vegetarian and with meat recipes:
  `/users/{id}/deletePicture?pictureId=`

## Live Demo
- Web app live demo: https://am-spring-app-recipe-website.azuremicroservices.io/

## App Details

- SpringBoot application
- MySQL database
- BootStrap Library
- HTML
- CSS
- JS
- Model Mapper
- Maintenance interceptor - the recipe website should be closed due to maintenance every day between 11:30 and midnight.
  The interceptor checks the current time and if it is maintenance time, redirects to the "/maintenance" endpoint.
- Scheduler: 
  - the application sends to the admins automatically generated message wirh the count of all registered users,
    the counts of the vegan, vegetarian and meat recipes every day at midnight.
  - the application changes the index page message and pictures every day at 11:00, 15:00, 17:00 and 00:00 oclock
    showing three random lunch, dinner, afternoon desert and breakfast recipes.
  - a scheduled task cleans up the database from expired tokens and users not verified profiles on every 30 mins.
- Cloudinary - the application uses Cloudinary for image upload.
- Exception handling - via @ControllerAdvice and @ExceptionHandler.


## Screenshots

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/70bf8ef0-a3a8-4c29-a560-6fc02a2b38ab)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/66aebc3b-e98f-4f59-a6c1-4c46627cf98d)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/ef274e13-9e9d-49c8-8b13-0910af1c81e1)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/687549c1-d3a7-456b-a7ed-807d604825fa)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/bc069830-51bf-44b1-8868-817d21aad5f6)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/b9cbcb5b-3941-4f66-aa1b-57c9c4ff6916)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/2207a017-958f-4fc0-9ada-7c8be2f7b27f)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/fb316bc1-32f7-4b15-8b3f-58c6e706f0cc)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/4bd829a7-af98-49d2-bffd-33d8ea7b2993)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/c8eb4fb7-df9e-486e-b4ea-1ed4f308b4c4)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/c50fc47e-af1d-4035-928a-37d8311a778b)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/2b172031-ee11-4a0e-ac81-7beae7b78819)

![image](https://github.com/98AnnaM/recipe-website-project/assets/147516467/957a846e-0cd7-4cc9-86bd-f041c8494e92)

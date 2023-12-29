# health_tracker_rest
Health Tracker REST

This is Health Tracker Rest app for write and read data about health. 
It includes 6 features - user, activity, product, dish, purchase and sleep. All these features include GET, POST, PATCH, DELETE requests.
User data includes Id;Name;Email parameters. It is possible to get all users by id and email.
Activity data includes Id;Description;Duration;Calories;Started;UserId parameters. It is possible to get activities by id, user id, year and month.
Product data includes Name;Calories;Proteins;Fats;Carbohydrates parameters. It is possible to get products by name and by greater/lower calories.
Dish data includes Name;Ingredient;Weight;Calories parameters. It is possible to get dishes by name and ingredient calories.
Purchase data includes Id;UserId;ProductName;Price;Date parameters. It is possible to get purchases by id, product name, user id, year, month, greater/lower price.
Sleep data includes Id;Duration;Date;UserId parameters. It is possible to get sleeps by id, user id, year and month.

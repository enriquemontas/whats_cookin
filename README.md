# What's Cookin'?

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app allows the user to represent their fridge/pantry and its contents virtually. A user can add, view, and remove items from it. There is also a built-in calorie counter allowing the user to estimate their caloric intake for the day. A later feature a page where the user can designate certain ingredients and view suggested recipes.

### App Evaluation
- **Category:** Liftsyle/Health
- **Mobile:** All the development will initially be Android focused. The idea seems inconvenient to use on a desktop by nature, and the design keeps mobile users as the priority.
- **Story:** The app allows users to keep track of what they currently have in their fridge, and then view and choose a recipe while keeping track of their estimated caloric intake.
- **Market:** The fitness industry is vast, and there are several apps designated to be a calorie counter. However, in my experience, they are annoying to use and leave me frustrated. So this app aims to be more convenient while offering the functionality of viewing the fridge and finding new recipes.
- **Habit:** The fridge is something most people use daily, so attempting to use the app commits the user to daily usage. If they want the calorie information to be accurate, usage will be multiple times a day to add meals quickly after eating them. When grocery shopping is another great time to open the app and make sure the user buys everything they need. 
- **Scope:** A stripped-down version of the app is both doable and exciting while still offering the perks mentioned above to the average user. I think everything outlined in the [Overview](#Overview)  is feasible during the 4-week FBU window. Adding more advanced features like workout tracking is easy to visualize with the theme of the app and mesh will with the target market.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Your app has multiple views
* Your app interacts with a database (e.g. Parse)
* You can log in/log out of your app as a user
* You can sign up with a new user profile
* Somewhere in your app you can use the camera to take a picture and do something with the picture (e.g. take a photo and share it to a feed, or take a photo and set a user’s profile picture)
* Your app integrates with a SDK (e.g. Google Maps SDK, Facebook SDK)
* Your app contains at least one more complex algorithm (talk over this with your manager)
* Your app uses gesture recognizers (e.g. double tap to like, e.g. pinch to scale)
* Your app use an animation (doesn’t have to be fancy) (e.g. fade in/out, e.g. animating a view growing and shrinking)
* Your app incorporates an external library to add visual polish


<!-- **Optional Nice-to-have Stories**

* [fill in your required user stories here]
* ... -->

### 2. Screen Archetypes

* Login/Signup
* Stream items in fridge and pantry
* Detail view of a selected item
* Creation add an item to the fridge
* View expected caloric intake
* Search Recipes with designated ingredients

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* View Fridge
* View expected calore intake
* Search recipe page

**Flow Navigation** (Screen to Screen)

* Login/Signup
   * Skipped via percisted login
   * Takes user to their fridge
* Fridge/Pantry Stream
   * Click for a detailed view
   * Add/Remove from the fridge
* Search Recipe
   * Select ingredients
   * Select search result to take you to the recipe page
* View Calories
   * None

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://imgur.com/a/24jeGSj" width=600>

## Schema 

### Models
Food Item
|Propety|Type|Desc|
|-------|----|----|
|objectID|String|Unique identifier for Parse|
|owner|pointer to User|User that owns given item|
|image|File|image to display for item|
|createdAt|DateTime|default field, used to estimate days in fridge|
|expirationDate|DateTime|not required used for dairy to display expiration dates|
|updatedAt|DateTime|date when post is last updated (default field)|
|barcode|Integer|For now only supporting UPC codes for more info|
|calories|Integer|Estimated calories for a displayed serving|


### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]

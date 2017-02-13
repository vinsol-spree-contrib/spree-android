# Spree Android App
Android application built to consume spree commerce api, is a front-end product store consumer centric
application that facilitates listing of banners, products, cart, review/rating, social signup, user profile.

It uses a customized version of [spree](https://github.com/spree/spree), available [here](https://github.com/vinsol-spree-contrib/shopspree-sales-app).

For demo configure the following backend api url, deployed at heroku http://shop-spree.herokuapp.com/api/ams/ 

## Compatibility
- minSdkVersion 17 (Android 4.2)
- targetSdkVersion 23 (Android 6.0)

## Configuration/Installation
Make changes to the following files:

Constants.java
- set up base api url 
- external links for Contact us, FAQs etc.
- Stripe app key
- Country id - hardcoded for US (change it as per requirement)

Strings.xml
- For social signup / signin : add FB APP ID (create your app on https://developers.facebook.com/)

## Screenshots

![Dashboard](https://dl.dropboxusercontent.com/u/4338625/shopspree/Home.png "Dashboard screen")

![Side Drawer](https://dl.dropboxusercontent.com/u/4338625/shopspree/Side%20drawer.png "Side drawer")

![Product Listing](https://dl.dropboxusercontent.com/u/4338625/shopspree/Product%20Listing.png "Product Listing screen")

![Cart](https://dl.dropboxusercontent.com/u/4338625/shopspree/Cart.png "Cart screen")

## Contributors
- [Vaibhav Khanna](https://github.com/v4ibh4v)
- [Alok Vaish](https://github.com/vaishalok)
- [Achin Kumar](https://github.com/infernus666) 
- [Hemant khemani](https://github.com/contacthrk)


## Maintainer
See [Vaibhav Khanna](https://github.com/v4ibh4v)

## TODOs
- Search
- Wishlist
- Notifications
- Products sorting
- Product Share


## License

[LICENSE](https://github.com/vinsol/spree-android/blob/master/LICENSE.md)


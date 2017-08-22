# SLYDEPAY ANDROID SDK
This is a sample project to show how to use the slydepay sdk to accept mobile money from several networks and credit card payments in your android apps.

# PREVIEW
----

| ![Payment Options](https://github.com/norrisboat/SlydepayAndroidREST/blob/master/Screenshots/payment_optioons.png?raw=true "Payment Options")  | ![Mobile Money](https://github.com/norrisboat/SlydepayAndroidREST/blob/master/Screenshots/mobile_money.png?raw=true "Mobile Money")  | ![Visa](https://github.com/norrisboat/SlydepayAndroidREST/blob/master/Screenshots/visa1.png?raw=true "Visa") | ![success](https://github.com/norrisboat/SlydepayAndroidREST/blob/master/Screenshots/success.png?raw=true "Transaction successful") | ![Error occured](https://github.com/norrisboat/SlydepayAndroidREST/blob/master/Screenshots/error.png?raw=true "Error occured")|

# Skip this if you have an active Merchant Account
-------------------------------------------------

Create a Merchant Account on Slydepay

Follow the link to get started on that:
([Slydepay Merchant Account](https://app.slydepay.com.gh/auth/signup#business_reg))

After the account has been verified and recognised as a Merchant Account.
Guess what?
You are good to go. Wait a minute

# How to install
---------------------------------------
To use the sdk in your project:

1. Download the project zip file.
2. Unzip the file.
3. In android studio right file -> new - import module and go to the path for the zip file -> open the folder -> and click on paywithmobilemoney.
4. Change the name of your module if you wish and click next.
5. Wait till the build finishes and you now have the paywithslydepay sdk.
    

From Android Studio :
```
->Right click your project folder 
        ->Open Module Settings 
                ->Select the Dependency Tab 
                          ->Tap on the green plus sign on the right and select "Module dependency" 
                                    ->You would see the "paywithslydepay" module. Select it.
```

Tap on OK and you are good to add it in your build.gradle file.

# Usage
---------------------------------------

1. Initialize sdk preferably in your Application class to avoid initializing it multiple time. 
[How to create an Application class](https://github.com/codepath/android_guides/wiki/Understanding-the-Android-Application-Class)
```java
//all parameters are Strings
//username and password refers to the credentials associated with your merchant account from MTN
  new SlydepayPayment(YourActivity.this).initCredentials.initCredentials(merchantEmail,merchantKey);
```

2. Performing transactions.
```java
//Performing transaction with provided ui
PayWithSlydepay.Pay("itemName",amount,"description","customerName","customerEmail","orderCode","phoneNumber",requestCode);
 
 //Listen for results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==YOUR_REQUEST_CODE)
            {
                switch (resultCode){
                    case RESULT_OK:
                        //Payment was successful
                        break;
                    case RESULT_CANCELED:
                        //Payment failed
                        break;
                    case RESULT_FIRST_USER:
                        //Payment was cancelled by user
                        break;
                }
        }
    }
```
**You can now recieve payment through your app with Slydepay**

# Special Thanks

# TODO
* Write tests.
* Singleton approach


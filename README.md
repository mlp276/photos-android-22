# Miguel Pagador AI used: ChatGPT

## How I used the GenAI
I used ChatGPT in order to ask prompts about small portions of the code of the project; rather than looking at big and creating entire Java files, I focus on the methods that can be implemented easily using GenAI. From there, I let ChatGPT take the context of what I prompted it to continue creating more methods and better refinements to the requirements of the project. That way, I can focus on small tasks from a design perspective and not have to consider the implementation in Java too heavily.

## Prompts
### Chat 1
"In Android Studio, how are files loaded and how does the user put in the file information for the application to use?"
"In the android app, I have a list of albums in the home activity. How can I load the names of the albums in a list view in the home acitivty?"

### Chat 2
"Given a ListView in Android Studio, how can I add options to it upon clicking it, such as adding, renaming, or deleting the list item?"
"When I do the switch case with item.getItemId(), each case has an error of a constant expression required. What does that mean?"
"In this application, Im using a data structure containing a list of albums. I want to display the list as a string, but then have the popup menu modify the actual list of albums data structure. How can I do that?"
"How can I show a confirmation dialog for deletion?"
"When I open an album, how can I switch activities?"
"I want to use a button in order to prompt to add a new album. The only input to adding a new album is the name of the new album. How can I do that?"
"How can I show an error pop up when the addition of the album failed in the albums.add(new Album(name))?"
"When switching to the next page, such as opening an album, how can I pass the Album reference to be used in the page?"
"How would the Parceable option work?"
"Both the Serializable and Parceable options are considered deprecated in the recent Android API 36. What would work in this API?"
"How can I abort the program upon a fatal condition met, such as a null pointer?"
"In an album, there are a list of photos that have a File object in each of them. How can I display the thumbnail images of the photos on Android?"
"The application may save photos to the device's gallery. Here's some info on how to access files (including photos) from storage: https://developer.android.com/guide/components/intents-common#Storage . From that, how should the Photo class be modified to contain the information to display the photo from the file?"
"I want to be able to save an application's state, including the user's albums and photos. How can I do that in Android?"
"Does this work for loading a previous session?"
"How should I use the AppState to load the session upon launch of the application?"
"I need to store the URIs in the photo class instead of the File. How can I change the PhotoAdapters to use the URIs?"
"I want to have an option to add a photo upon pressing a button, and prompting the user to provide the photo to add. How can I do that?"
"How can I add photos to the Android Studio emulator in order to select it and add it to the app?"

### Chat 3
"Without using external libraries in Android Studio, how can I ask the user to get a photo from the device when, for example, pressing a button?"

### Chat 4
"How can I save an app's session in Android Studio without using external dependencies?"
"Could you specify the JSON method of saving an app's state?"
"I cannot use an external library to display a photo in an Android Studio app. What's the best way to display a photo given the URI?"
"How can I load a list of photos as a list/grid on the screen?"
"How can I make the view to include as many photos?"
"There seems to be a big gap between photos in the image view. What do you think causes this?"
"Here are the layout XMLs." (provided xml files from Android Studio in the prompt)
"How can I make it so that when I click on an image, it gives me options to add, remove, or display a photo in a bigger view?"

### Chat 5
"How can I pass a class from one activity to another in Android Studio?"
"When I switch to another activity, I need to be able to save the app if it closes in that other activity, and I need to save an overarching AppState object in all the activities. How can I do that?"
"Let's say that I have a list of albums in the home page, and then I pass an album into the album details page. The Home activity will have List<Album>, while the album details page has the Album class, and an Intent was used to pass between activities. How can I go back to the home page with the previous state of the Home page with its list of albums?"

### Chat 6
"I am working with Android Studio, and I am exclusively using Java to work with the classes in the platform. Let's say that I have a list of albums in the home page represented by the Home activity, and then move to the album details page represented by the AlbumDetails activity of the opened album. The Home activity will have a List<Album> object, while the album details page has Album object that was opened, and an Intent was used to pass between activities. How can I properly get the data structures between the two pages, to go one way and to go the other way?"
"Do I need a dedicated button to go back to the home page in Android Studio? How does "going back" work in Android?"
"When I go back to the Home Page and then try to open an album to display the list of photos using the URIs that were added in the AlbumDetails page, the application crashes. Why does this happen?"
"Let's say that I have another activity to display the photo in full view. How can I do that using an ImageView and the URI provided in the Photo class?"

### Chat 7
"How can I set an Image View to the photo given by the URI in Android Studio?"
"If I want to update the photo in the image View, how can I do it?"

### Chat 8
"How can I get the file name of a URI in Android Studio?"

### Chat 9
"Given an AppState object, how can I carry over the AppState when switching activities in an Android Studio app using Java?"
"When the app closes, how can I save the app state to be used for the next session by using Gson?"
"I want to save the URI through GSON, but it seems it does not work loading that. What might be the error here?"
"Does making the URI field of Photo transient block it from being attempted to be seralized?"
"How can I clear the JSON file generated after saving the app state?"
"How would I do that in the Android Studio Emulator?"

### Chat 10
"Let's say that we have photos with tags, given by a name and a value, and the tags must belong to a tag type defined in the Android Studio application. I want to prompt the user to ask for different options of searching photos by tags: (1) search by one tag with a value only, (2) search by the presence of both tags, and (3) search by the presence of either tag. How would I generate a Predicate<Tag> object based on the user's prompt of these three options?"

### Chat 11
"When I pop up an AlertDialog.Builder, how do I wait until I get its input?"
"I want to make an AlertBuilder.Dialog, but it will only be used if it meets a certain global condition. I want the AlertBuilder.Dialog to wait for the user's input if it meets the condition; otherwise, it will do something else. How can I do that?"
"If the Dialog was cancelled for some reason, how would I check it so I can do proper cleanup of certain data?"

## UI Drawings Prompted
I did not use UI drawings to prompt to ChatGPT for this project.

## Code Components Usage
A good portion of the front-end that I implemented for Android Studio were AI generated. This included the many methods that are in each of the activity used in the application. I also generated the utility Java files to assist with the activity files, such as PhotosApplication.java and AppState.java. On the other hand, the backend was mostly AI generated but also hand-written because I wanted to make sure that the backend's logic is extremely solid for valid use cases in the application.

# Philip Guan AI used: Android Studio Agent Google Gemini

## Tags prompts:

Can you explain what Tag.java and TagType.java currently do?
What type of tags are allowed?
Can you make it so that only person and location tag types are allowed?
Can you add a button to the photo display page that allows the user to add tags to the photo? Make sure the tags are displayed on the photo display page.
Can you add a button to the photo display page that allows the user to remove tags to the photo? The button opens a drop down menu of existing tags and the tag you select will be removed. If there are no existing tags, the remove photo button does nothing.

## Move photo prompts:

Can you add a button that in the Photo options page that allows the user to move a photo from one album to another?

## Search tags prompts:

Can you add a Search Tags button in top right corner of the home page. When this button is clicked, a drop down menu appears that allows you to choose between person and location tags. After one of those are selected, the user can type the name of the tag they are searching for and all photos that match the same tag name will appear. If there are no photos with that tag name, the page is empty.
When searching for tags, if there are no tags with the same name as the searched tag, do not close the app. Just say that "No photos have this tag"
Can you make it so that for searching 1 tag, when you enter a tag value it opens a drop down menu where you can select from a list of tags that have the starting substring that you searched for.
For searching 2 tags, for both times when you enter a tag value it opens a drop down menu where you can select from a list of tags that have the starting substring that you searched for.
In these drop down menus for both cases, when you select one of those existing tags, then that is the tag it searches for. If there are no tags with that starting substring, then no drop down menu appears.

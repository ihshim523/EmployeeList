## Build tools & versions used

Android Studio Bumblebee | 2021.1.1 Patch1
Minimum SDK: 26

## Steps to run the app

1. Open project
2. Build and run on emulator or physical device

## What areas of the app did you focus on?

- Clear separation of concerns with 3-tier design pattern between view, view model and data repository (MVVM).
- View model retains data presented in the view and manages saved instance to survive system view destroyed scenario such as
configuration change.
- With passive view principle, view passively renders the view state emitted from the view model. This minimizes amount of business logic in views
- By letting view model contains business logic, testing with unit test on the view model will cover most of functionality and reduce chance of unexpected error.

## What was the reason for your focus? What problems were you trying to solve?

During app development, developers have to accept many requirement changes and produce scalable, performant and bug free code in time.
Decoupling between components and modules is a key to increase adaptability to the change.
Also maintaining one-way communication between view and view model simplifies code path and enhance code readability.

## How long did you spend on this project?

6 hours in spanning a week

## Did you make any trade-offs for this project? What would you have done differently with more time?

Due to time constraint, I couldn't spend time on applying modern UI components.
If I would have more time, would like to try Compose UX library.

## What do you think is the weakest part of your project?

No paging support is implemented so if data set is large, app will perform slower.
No modularization or dependency injection support.

## Did you copy any code or dependencies? Please make sure to attribute them here!

Retrofit by Square
Picasso by Square
Influenced by project of current company on a utility extension function (savedState)

## Is there any other information you’d like us to know?

happy coding!

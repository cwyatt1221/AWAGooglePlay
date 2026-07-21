# Animal Wellness Action — Mobile App

An Android app that displays animalwellnessaction.org in-app, with a background
job that syncs a snapshot of key pages once a day so the app still has content
to show if the phone loses connection.

## What it does

- Loads the live site in a WebView, so the app always reflects the current
  site with no extra work when you update WordPress.
- Once every 24 hours (via `WorkManager`), it re-downloads the pages listed in
  `SyncWorker.PAGES_TO_SYNC` and saves them to local storage.
- If the device is offline, requests for those specific pages are served from
  the last saved snapshot instead of failing.
- Pull-to-refresh reloads the current page and kicks off an extra sync.

This is a **wrapper + offline-cache app**, not a full native rewrite — the
fastest path to a real app in Google Play. If you later want native screens
(profile lists, search, filters) that pull structured data instead of
rendering the website, that's a bigger build using the site's REST API — let
me know and I can scope that separately.

## Before you build

1. **Pick the pages that matter most for offline use.** Open
   `app/src/main/java/org/animalwellnessaction/app/SyncWorker.kt` and edit
   `PAGES_TO_SYNC`. It currently includes the homepage, the Congressional
   Profiles page, and the Endorsements page.
2. **Replace the app icon.** Right now `ic_launcher.xml` points at a system
   placeholder icon. In Android Studio: right-click `res` → New → Image Asset,
   and use AWA's logo.
3. **Confirm the app name** in `app/src/main/res/values/strings.xml`
   (`app_name`).

## Building it

You'll need [Android Studio](https://developer.android.com/studio) (this
can't be compiled in this chat — Android SDKs aren't available here).

1. Unzip this project and open the folder in Android Studio.
2. Let Gradle sync (it will download the Android Gradle Plugin and
   dependencies automatically).
3. Click **Run** to test on an emulator or a connected device.

## Preparing a release build for Google Play

Google Play requires an **Android App Bundle (.aab)**, signed with your own
key.

1. In Android Studio: **Build → Generate Signed Bundle / APK → Android App
   Bundle**.
2. Create a new keystore (first time) — store the `.jks` file and its
   passwords somewhere safe and backed up. If you lose this key, you can
   never update the app again under the same listing.
3. Build the **release** variant. This produces an `.aab` file.

## Publishing to Google Play — checklist

1. **Google Play Developer account** — one-time $25 registration at
   [play.google.com/console](https://play.google.com/console), tied to a
   Google account. Takes Google anywhere from a few hours to a couple of days
   to verify.
2. **Privacy policy URL** — required for every app, even simple ones. A
   starter template is in `PRIVACY_POLICY.md` in this folder — publish it as
   a page on animalwellnessaction.org and use that URL.
3. **Store listing assets**:
   - App icon (512×512 PNG)
   - Feature graphic (1024×500 PNG)
   - At least 2 phone screenshots (take these from the emulator or a device)
   - Short description (80 chars) and full description (4000 chars)
4. **Content rating questionnaire** — filled out in the Play Console; a
   straightforward advocacy/news-content app is usually a quick "Everyone" or
   "Teen" rating.
5. **Data safety form** — Play Console asks what data the app collects. This
   app itself doesn't collect anything beyond standard network requests to
   load the site; if the site sets analytics or cookies, reflect that here.
6. **Upload the `.aab`**, complete the listing, and submit for review.
   Google's review typically takes a few hours to a few days for a first
   submission.

## Updating the app later

Any time you want to change what's cached, the icon, or the app name, edit
the relevant file, bump `versionCode` and `versionName` in
`app/build.gradle.kts`, rebuild a signed bundle, and upload it as a new
release in the Play Console — the WebView content itself updates
automatically with no app update needed, since it just loads the live site.
# AWAGooglePlay

package com.potatodevs.cropsamarica.ui.guide

import androidx.annotation.DrawableRes
import com.potatodevs.cropsamarica.R


data class Guide(
    val step : String,
    val description : String,
    @DrawableRes val image : Int,
    val action : String ?,
    val checkList : List<String>
)
val STEP_1 = Guide(
    step = "Step 1",
    description = "Open the App You will see the home screen with two options:",
    image = R.drawable.step1,
    action = "Tap Register",
    checkList = listOf("Login","Register")
)
val STEP_2 = Guide(
    step = "Step 2",
    description = "Fill Out the Registration FormYou will be asked to provide the following information:",
    image = R.drawable.step2,
    action = null,
    checkList = listOf(
        "Pangalan – Enter your full name.",
        "City / Municipality – Indicate where you live.",
        "Barangay - Your current barangay of residence.",
        "Phone Number –  your active phone number."
    )
)
val STEP_3 = Guide(
    step = "Step 3",
    description = "Enter the Verification Code. You will receive a 6-digit code on your registered phone number.",
    image = R.drawable.step3,
    action = "Tap Verify to continue",
    checkList = listOf(
        "Check your inbox or SMS.",
        "Type the verification code in the field provided."
    )
)
val STEP_4 = Guide(
    step = "Step 4",
    description = "Set Up Your Farm. After completing the verification process, you can now create your farm profile.",
    image = R.drawable.step4,
    action = "Tap Create",
    checkList = listOf(
        "Enter the basic details needed to start your farm profile.",
        "Proceed to the next steps to complete the full farm information.",
        "This begins the registration of your farm in the system."
    )
)

val STEP_5 = Guide(
    step = "Step 5",
    description = "Fill Out the Farm Details. Provide the required information about your farm by completing each field.",
    image = R.drawable.step5,
    action = "Tap Submit to proceed",
    checkList = listOf(
        "Farm Name",
        "Municipality",
        "Province",
        "Planted Date",
        "Area Size",
        "Variety",
        "Soil Type",
        "Gravity Irrigation",
        "Upload a photo of your farm or field area"
    )
)
val STEP_6 = Guide(
    step = "Step 6",
    description = "View Your Farm Dashboard. After submitting your details, you will be directed to your Farm Dashboard where you can see an overview of your farm information.",
    image = R.drawable.step6,
    action = null,
    checkList = listOf(
        "Farm Name: Abel Farm",
        "Location: Magsaysay, Occidental Mindoro",
        "Planted Date: October 7, 2025",
        "Area Size: 1.5 hectares",
        "Variety: NSIC Rc 160",
        "Soil Type: Clay Loam",
        "Irrigation: Gravity Irrigated",
        "Farm Image: Displayed as a thumbnail of the uploaded photo"
    )
)
val STEP_7 = Guide(
    step = "Step 7",
    description = "Receive Important Alerts. The system will notify you about important updates related to your farm.",
    image = R.drawable.step7,
    action = null,
    checkList = listOf(
        "Rain Alerts – Get notified if there is incoming rainfall in your area.",
        "Panicle Initiation Alerts – Know when your crops reach the panicle initiation stage.",
        "Current Task Alerts – View the recommended tasks you need to perform based on your crop stage."
    )
)
val STEP_8 = Guide(
    step = "Step 8",
    description = "View Pest and Disease Information. Access important details about pests and diseases that may affect your crops.",
    image = R.drawable.step8,
    action = null,
    checkList = listOf(
        "Common Pests in your area",
        "Disease Symptoms to watch out for",
        "Recommended Controls and preventive actions"
    )
)
val STEP_9 = Guide(
    step = "Step 9",
    description = "View Your Farm in My Task. Access the tasks assigned to your farm and monitor your progress.",
    image = R.drawable.step9,
    action = null,
    checkList = listOf(
        "See your farm listed under the My Task section.",
        "Check ongoing and upcoming tasks.",
        "Monitor progress based on your crop stage and system recommendations."
    )
)
val STEP_10 = Guide(
    step = "Step 10",
    description = "Open the User Menu. Access important account and system options from the user menu.",
    image = R.drawable.step10,
    action = null,
    checkList = listOf(
        "Edit Profile – Update your personal information and account details.",
        "User Guide – View instructions on how to use the system.",
        "Developers – See information about the system developers.",
        "Settings – Adjust app preferences and configurations."
    )
)
val GUIDES = listOf<Guide>(
    STEP_1,
    STEP_2,
    STEP_3,
    STEP_4,
    STEP_5,
    STEP_6,
    STEP_7,
    STEP_8,
    STEP_9,
    STEP_10
)

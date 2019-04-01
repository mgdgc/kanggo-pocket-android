package com.RiDsoft.kangwonhighschool.value

/**
 * Created by RiD on 2017. 4. 30..
 */

object Key {

    const val KEY_APP = "app"
    const val KEY_USER = "user"
    const val KEY_STUDENT_ID = "student_id"

    // belongs to "app" preferences
    const val BOOL_DEV_MODE = "boolean_dev_mode"

    // belongs to "default" preferences
    const val PREF_SHOW_STUDENT_ID = "pref_show_student_id"

    const val KEY_KPA = "kp_assist"

    const val KEY_KPA_RECOMMENDED = "kpa_recommended"

    // TeacherSchedule
    const val KEY_TEACHER = "teacher"
    const val OBJECT_TEACHER_SCHEDULE = "teacher_schedule"

    // belongs to "student_id" preferences
    const val STRING_BARCODE_TYPE = "barcode_type"
    const val STRING_BARCODE_DATA = "barcode_data"

    // belongs to "user" preferences
    const val KEY_USER_GRADE = "grade"
    const val KEY_USER_CLASS = "class"
    const val KEY_USER_NUMBER = "number"
    const val KEY_USER_NAME = "name"
    const val KEY_USER_PHONE_NUMBER = "phone_number"
    const val BOOL_GRADUATED = "boolean_graduated"

    // static values
    const val KEY_1ST_GRADE = "1st"
    const val KEY_2ND_GRADE = "2nd"
    const val KEY_3RD_GRADE = "3rd"

    const val KEY_MEAL = "meal"

    const val KEY_WEB = "web"
    //Belongs to "web" preference
    const val KEY_ID = "user_web_iD"
    const val KEY_PW = "user_web_pWd"
    const val KEY_LOGIN_MODE = "login_mode"
    const val KEY_AUTO_LOGIN = "auto_login"
    const val KEY_FINGERPRINT = "fingerprint_login"
    const val KEY_WRITING_TEMP = "writing_temp_exist"
    const val KEY_WRITING_TITLE = "writing_title"
    const val KEY_WRITING_CONTENT = "writing_content"

    //    PreferenceFragment
    const val PREF_SMALL_SCREEN_MODE = "pref_low_dpi"
    const val PREF_SMALL_SCREEN_RECOMMEND = "pref_low_dpi_recommend"
    const val PREF_THEME = "pref_theme"
    const val PREF_AIR_TOOL = "pref_air_tool"
    const val PREF_AIR_TOOL_ICON = "pref_air_tool_icon"
    const val PREF_AIR_TOOL_ICON_ALPHA = "pref_air_tool_icon_alpha"
    const val PREF_AIR_TOOL_BOOT = "pref_air_tool_boot"
    const val PREF_AIR_TOOL_NOTI_PRIORITY = "pref_air_tool_noti"
    const val PREF_TEXT_SIZE = "pref_text_size"

    const val KEY_AIR_TOOL = "air_tool"
    const val INT_Y = "y"
    const val INT_GRAVITY = "gravity" //0: left, 1: right
}

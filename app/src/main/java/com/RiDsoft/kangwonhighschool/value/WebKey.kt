package com.RiDsoft.kangwonhighschool.value

import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_BASEBALL_NOTI
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_BONGAM_HANDOUTS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_BUDGET_PARTICIPATE
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_FOOD_INFOS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_FREE_TALKS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_HANDOUTS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_KANGGO_AE
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_MEAL_CRITICS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_MEAL_DOCS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_NOTIFICATION
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_OFFICIAL_DOCS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_OPEN_ADMINISTRATION
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_OPERATATION_COUNCEL
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_QUESTIONS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_STUDENT_COUNCEL
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_STUDY_FILES
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.KEY_WRESTLING_NOTI
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_BASEBALL_NOTI
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_BONGAM_HANDOUTS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_BUDGET_PARTICIPATE
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_FOOD_INFOS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_FREE_TALKS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_HANDOUTS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_KANGGO_AE
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_MEAL_CRITICS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_MEAL_DOCS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_NOTIFICATION
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_OFFICIAL_DOCS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_OPEN_ADMINISTRATION
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_OPERATATION_COUNCEL
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_QUESTIONS
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_STUDENT_COUNCEL
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_STUDY_FILES
import com.RiDsoft.kangwonhighschool.value.WebKey.Companion.TITLE_WRESTLING_NOTI

/**
 * Created by RiD on 2017. 7. 2..
 */

class WebKey {

    val categoryArray: IntArray
        get() = intArrayOf(KEY_NOTIFICATION, KEY_STUDY_FILES, KEY_HANDOUTS, KEY_KANGGO_AE, KEY_FREE_TALKS, 0, KEY_NOTIFICATION, KEY_HANDOUTS, KEY_OFFICIAL_DOCS, KEY_OPERATATION_COUNCEL, KEY_OPEN_ADMINISTRATION, KEY_BUDGET_PARTICIPATE, 0, KEY_STUDY_FILES, KEY_BONGAM_HANDOUTS, KEY_STUDENT_COUNCEL, KEY_KANGGO_AE, 0, KEY_MEAL_DOCS, KEY_MEAL_CRITICS, KEY_FOOD_INFOS, 0, KEY_FREE_TALKS, KEY_QUESTIONS, 0, KEY_BASEBALL_NOTI, KEY_WRESTLING_NOTI)

    val categoryTitles: Array<String>
        get() = arrayOf(TITLE_NOTIFICATION, TITLE_STUDY_FILES, TITLE_HANDOUTS, TITLE_KANGGO_AE, TITLE_FREE_TALKS, "---알림마당---", TITLE_NOTIFICATION, TITLE_HANDOUTS, TITLE_OFFICIAL_DOCS, TITLE_OPERATATION_COUNCEL, TITLE_OPEN_ADMINISTRATION, TITLE_BUDGET_PARTICIPATE, "---학생마당---", TITLE_STUDY_FILES, TITLE_BONGAM_HANDOUTS, TITLE_STUDENT_COUNCEL, TITLE_KANGGO_AE, "---급식자료실---", TITLE_MEAL_DOCS, TITLE_MEAL_CRITICS, TITLE_FOOD_INFOS, "---참여마당---", TITLE_FREE_TALKS, TITLE_QUESTIONS, "---운동부---", TITLE_BASEBALL_NOTI, TITLE_WRESTLING_NOTI)

    companion object {

        //알림마당
        val KEY_NOTIFICATION = 19202 //공지사항
        val KEY_HANDOUTS = 19210 //가정통신문
        val KEY_OFFICIAL_DOCS = 19211 //학교양식
        val KEY_OPERATATION_COUNCEL = 19212 //학교운영위원회
        val KEY_OPEN_ADMINISTRATION = 19213 //공개행정
        val KEY_BUDGET_PARTICIPATE = 19215 //예산참여방

        //학생마당
        val KEY_STUDY_FILES = 19216 //교과학습자료실
        val KEY_BONGAM_HANDOUTS = 19217 //봉암소식지
        val KEY_STUDENT_COUNCEL = 19222 //학생회게시판
        val KEY_KANGGO_AE = 54576 //학교홍보대사

        //급식자료실
        val KEY_MEAL_DOCS = 19240 //급식, 보건자료실
        val KEY_MEAL_CRITICS = 19242 //급식소리함
        val KEY_FOOD_INFOS = 19243 //원산지 및 영양표시

        //참여마당
        val KEY_FREE_TALKS = 19245 //자유게시판
        val KEY_QUESTIONS = 19246 //질문있어요

        //운동부
        val KEY_BASEBALL_NOTI = 19240 //야구부 공지사항
        val KEY_WRESTLING_NOTI = 19240 //레슬링부 공지사항


        //알림마당
        val TITLE_NOTIFICATION = "공지사항"
        val TITLE_HANDOUTS = "가정통신문"
        val TITLE_OFFICIAL_DOCS = "학교양식"
        val TITLE_OPERATATION_COUNCEL = "학교운영위원회"
        val TITLE_OPEN_ADMINISTRATION = "공개행정"
        val TITLE_BUDGET_PARTICIPATE = "예산참여방"

        //학생마당
        val TITLE_STUDY_FILES = "교과학습자료실"
        val TITLE_BONGAM_HANDOUTS = "봉암소식지"
        val TITLE_STUDENT_COUNCEL = "학생회게시판"
        val TITLE_KANGGO_AE = "학교홍보대사"

        //급식자료실
        val TITLE_MEAL_DOCS = "급식/보건 자료실"
        val TITLE_MEAL_CRITICS = "급식소리함"
        val TITLE_FOOD_INFOS = "원산지 및 영양표시"

        //참여마당
        val TITLE_FREE_TALKS = "자유게시판"
        val TITLE_QUESTIONS = "질문있어요"

        //운동부
        val TITLE_BASEBALL_NOTI = "야구부 공지사항"
        val TITLE_WRESTLING_NOTI = "레슬링부 공지사항"
    }

}

package com.RiDsoft.kangwonhighschool.ui.schedule.student

class ScheduleObject {

    var grade: Int
    var classNumber: Int
    var week: Int
    var period: Int
    var subject: String
    var teacher: String
    var memo: String

    constructor() {
        grade = 0
        classNumber = 0
        week = 0
        period = 0
        subject = ""
        teacher = ""
        memo = ""
    }

    constructor(grade: Int, classNumber: Int, week: Int, period: Int, subject: String, teacher: String, memo: String = "") {
        this.grade = grade
        this.classNumber = classNumber
        this.week = week
        this.period = period
        this.subject = subject
        this.teacher = teacher
        this.memo = memo
    }

}
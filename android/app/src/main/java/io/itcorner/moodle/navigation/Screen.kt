package io.itcorner.moodle.navigation

import android.net.Uri

object NavArgs {
    const val COURSE_ID = "courseId"
    const val COURSE_TITLE = "courseTitle"
}

object Routes {
    const val LOGIN = "login"
    const val COURSES = "courses"
    const val GRADES = "grades"
    const val COURSE_DETAIL = "course/{${NavArgs.COURSE_ID}}?${NavArgs.COURSE_TITLE}={${NavArgs.COURSE_TITLE}}"

    fun courseDetail(id: Long, title: String): String =
        "course/$id?${NavArgs.COURSE_TITLE}=${Uri.encode(title)}"
}

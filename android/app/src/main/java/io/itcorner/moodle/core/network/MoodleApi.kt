package io.itcorner.moodle.core.network

import io.itcorner.moodle.core.network.dto.CourseDto
import io.itcorner.moodle.core.network.dto.GradeReportResponse
import io.itcorner.moodle.core.network.dto.LoginTokenResponse
import io.itcorner.moodle.core.network.dto.SectionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MoodleApi {

    @GET("webservice/rest/server.php?wsfunction=core_enrol_get_users_courses")
    suspend fun getCourses(@Query("userid") userId: Long): List<CourseDto>

    @GET("webservice/rest/server.php?wsfunction=core_course_get_contents")
    suspend fun getCourseContents(@Query("courseid") courseId: Long): List<SectionDto>

    @GET("webservice/rest/server.php?wsfunction=gradereport_user_get_grade_items")
    suspend fun getGradeItems(
        @Query("userid") userId: Long,
        @Query("courseid") courseId: Long,
    ): GradeReportResponse

    @GET("login/token.php?service=moodle_mobile_app")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String,
    ): LoginTokenResponse
}

package com.choijihyuk0609.plustalk1

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.choijihyuk0609.plustalk1.presentation.view.auth.AuthActivity
import org.junit.Rule
import org.junit.Test

class AuthActivityTest {

    // Activity를 테스트할 때 사용
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(AuthActivity::class.java)

    @Test
    fun testSigninFragmentIsDisplayed() {
        // SigninFragment가 fragment_container에 잘 붙었는지 확인
        onView(withId(R.id.fragment_container))
            .check(matches(isDisplayed())) // fragment_container가 잘 표시되는지 체크
    }
}
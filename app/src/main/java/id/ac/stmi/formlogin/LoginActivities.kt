package id.ac.stmi.formlogin

/** Form Login versi LinearLayout. */
class LinearLoginActivity : BaseLoginActivity() {
    override val layoutRes = R.layout.activity_login_linear
}

/** Form Login versi RelativeLayout. */
class RelativeLoginActivity : BaseLoginActivity() {
    override val layoutRes = R.layout.activity_login_relative
}

/** Form Login versi ConstraintLayout. */
class ConstraintLoginActivity : BaseLoginActivity() {
    override val layoutRes = R.layout.activity_login_constraint
}

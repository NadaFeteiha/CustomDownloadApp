package com.nadafeteiha.customdownloadapp

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var widthSize = 0
    private var heightSize = 0

    private val buttonAnimationDuration = 10000L
    private val iconAnimationDuration = 5000L

    private var buttonBackgroundColor = 0
    private var loadingBackgroundColor = 0
    private var iconLoadingColor = 0

    private var buttonText = resources.getString(R.string.button_name)
    private var buttonWidthLoading = 0F
    private var iconCircleAngleLoading = 0f

    private var buttonAnimator = ValueAnimator()
    private var iconCircleAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat()).apply {
                    duration = buttonAnimationDuration
                    interpolator = DecelerateInterpolator()
                    addUpdateListener {
                        buttonWidthLoading = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }

                iconCircleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
                    duration = iconAnimationDuration
                    buttonText = context.getString(R.string.button_loading)
                    interpolator = AccelerateInterpolator(1f)
                    addUpdateListener {
                        iconCircleAngleLoading = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                    start()
                }

            }
            ButtonState.Completed -> {
                buttonWidthLoading = 0F
                iconCircleAngleLoading = 0f
                buttonText = context.getString(R.string.button_download_complete)
                buttonAnimator.end()
                iconCircleAnimator.end()
                setButState(ButtonState.Clicked)

            }
            else -> {
                ButtonState.Clicked
                buttonWidthLoading = 0f
                iconCircleAngleLoading = 0f
                buttonText = context.getString(R.string.button_name)
                buttonAnimator.cancel()
                iconCircleAnimator.cancel()
            }

        }
    }

    fun setButState(state: ButtonState) {
        buttonState = state
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackgroundColor = getColor(
                R.styleable.LoadingButton_backgroundColor,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            loadingBackgroundColor = getColor(
                R.styleable.LoadingButton_LoadingBackgroundColor,
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            iconLoadingColor = getColor(
                R.styleable.LoadingButton_iconLoadingColor,
                ContextCompat.getColor(context, R.color.colorAccent)
            )
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawButton(canvas)
        drawButtonLoading(canvas)
        drawText(canvas)
        drawCircle(canvas)
    }

    private fun drawButton(canvas: Canvas) {
        paint.color = buttonBackgroundColor
        val rectF = RectF(Rect(0, 0, width, height))
        val round = context.resources.getDimension(R.dimen.button_round)
        canvas.drawRoundRect(rectF, round, round, paint)
    }

    private fun drawButtonLoading(canvas: Canvas) {
        val rect = RectF(Rect(0, 0, buttonWidthLoading.toInt(), height))
        paint.color = loadingBackgroundColor
        val round = context.resources.getDimension(R.dimen.button_round)
        canvas.drawRoundRect(rect, round, round, paint)
    }

    private fun drawText(canvas: Canvas) {
        paint.color = Color.WHITE
        val size = context.resources.getDimension(R.dimen.button_font_text_size)
        paint.textSize = size
        val posTextX = (width.toFloat() / 2)
        val posTextY = (height.toFloat() / 2) + (size / 4)
        canvas.drawText(buttonText, posTextX, posTextY, paint)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.color = iconLoadingColor
        val radius = 0.50F
        val posX = (height / 2).toFloat()
        val posY = width.toFloat()
        val defaultMargin = context.resources.getDimension(R.dimen.default_margin)
        val circleIcon = RectF(
            posY - 150,
            posX - (posX * radius),
            posY - (defaultMargin * 2),
            posX + (posX * radius)
        )

        canvas.drawArc(
            circleIcon,
            0f,
            iconCircleAngleLoading,
            true,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}
package code.with.cal.kotlincalculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.ddona.caculator_android_intern.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()
{
    private var canAddOperation = false
    private var canAddDecimal = true
    private var checkdot = true




    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun screen_rotation(view: View){

    }

    fun numberAction(view: View)
    {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    workingsTV.append(view.text)

                canAddDecimal = false
            }
            else
                workingsTV.append(view.text)

            canAddOperation = true
        }
    }

    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation)
        {
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true

        }
    }

    fun allClearAction(view: View)
    {
        workingsTV.text = ""
        resultsTV.text = ""
        canAddOperation = false
        canAddDecimal = true
    }

    fun backSpaceAction(view: View)
    {
        val length = workingsTV.length()
        if(length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)

        canAddOperation = false
        canAddDecimal = true
    }

    fun equalsAction(view: View)
    {
        resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val Operators = Operators()
        if(Operators.isEmpty()) return ""

        val Division = DivisionCalculate(Operators)
        if(Division.isEmpty()) return ""

        val result = addSubtractCalculate(Division)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit

            }
        }

        return result
    }

    private fun DivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/') || list.contains('%') )
        {
            list = calcDiv(list)
        }
        return list
    }

    private fun calcDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float


                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    '%' ->
                    {
                        newList.add(prevDigit / 100)
                        restartIndex = i + 1

                    }


                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun Operators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workingsTV.text)
        {
            if(character.isDigit() || character == '.' )
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

}
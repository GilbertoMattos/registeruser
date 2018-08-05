package br.com.gilberto.registeruser

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var inputLay_nome_completo: TextInputLayout
    private lateinit var inputLay_data_nascimento: TextInputLayout
    private lateinit var inputLay_email: TextInputLayout
    private lateinit var inputLay_senha: TextInputLayout
    private lateinit var inputLay_repete_senha: TextInputLayout
    private lateinit var inputLay_sexo: TextInputLayout
    private lateinit var inputLay_profissao: TextInputLayout
    private lateinit var inputLay_estadoCivil: TextInputLayout

    private lateinit var edit_nome_completo: AppCompatEditText
    private lateinit var edit_email: AppCompatEditText
    private lateinit var edit_senha: AppCompatEditText
    private lateinit var edit_repete_senha: AppCompatEditText
    private lateinit var edit_data_nascimento: AppCompatEditText
    private lateinit var spinner_sexo: AppCompatSpinner
    private lateinit var spinner_profissao: AppCompatSpinner
    private lateinit var spinner_estadoCivil: AppCompatSpinner

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar_include)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        inputLay_nome_completo = findViewById(R.id.til_nome_completo)
        inputLay_data_nascimento = findViewById(R.id.til_data_nascimento)
        inputLay_email = findViewById(R.id.til_email)
        inputLay_repete_senha = findViewById(R.id.til_repeat_senha)
        inputLay_senha = findViewById(R.id.til_senha)
        inputLay_sexo = findViewById(R.id.til_sexo)
        inputLay_profissao = findViewById(R.id.til_profissao)
        inputLay_estadoCivil = findViewById(R.id.til_estadocivil)


        edit_nome_completo = findViewById(R.id.edt_name_completo)
        edit_email = findViewById(R.id.edt_email)
        edit_repete_senha = findViewById(R.id.edt_repeat_senha)
        edit_senha = findViewById(R.id.edt_senha)
        edit_data_nascimento = findViewById(R.id.edt_data_nascimento)
        spinner_sexo = findViewById(R.id.edt_sexo)
        spinner_profissao = findViewById(R.id.edt_profissao)
        spinner_estadoCivil = findViewById(R.id.edt_estadocivil)

        edit_data_nascimento.keyListener = null

        populationSpiners()

        btn_salvar.setOnClickListener {
            validarForm()
        }

        edit_data_nascimento.setOnClickListener {
            selectDate()
        }

        edit_data_nascimento.onFocusChangeListener = View.OnFocusChangeListener { view: View, b: Boolean ->
            if (b) {
                selectDate()
            }
        }
    }

    private fun selectDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> edit_data_nascimento.setText("$dayOfMonth/${monthOfYear + 1}/$year") }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {


    }


    private fun populationSpiners() {

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexoArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_sexo.adapter = adapter

        val adapterEstadoCivil = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estadoCivilArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_estadoCivil.adapter = adapterEstadoCivil

        val adapterProfissao = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, profissaoArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_profissao.adapter = adapterProfissao
    }

    fun sexoArray(): ArrayList<String> {
        return arrayListOf("Mascunilo", "Feminino")
    }

    fun estadoCivilArray(): ArrayList<String> {
        return arrayListOf("Solteiro", "Casado", "Divorciado")
    }

    fun profissaoArray(): ArrayList<String> {
        return arrayListOf("Desenvolvedor", "Analista", "Ator")
    }

    private fun validarForm() {

        if (edit_nome_completo.text.toString().isEmpty()) {
            inputLay_nome_completo.isErrorEnabled = true
            inputLay_nome_completo.error = "Por favor, preencher o campo nome completo"
        } else {
            inputLay_nome_completo.isErrorEnabled = false
        }

        if (edit_email.text.toString().isEmpty()) {
            inputLay_email.isErrorEnabled = true
            inputLay_email.error = "Por favor, preencher o campo email"
        } else {
            inputLay_email.isErrorEnabled = false
        }


        if (edit_senha.text.toString().isEmpty()) {
            inputLay_senha.isErrorEnabled = true
            inputLay_senha.error = "Por favor, preencher o campo senha"
        } else {
            inputLay_senha.isErrorEnabled = false
        }


        if (edit_repete_senha.text.toString().isEmpty()) {
            inputLay_repete_senha.isErrorEnabled = true
            inputLay_repete_senha.error = "Por favor, confirmar a senha"
        } else {
            inputLay_repete_senha.isErrorEnabled = false
        }

        if (!inputLay_senha.isErrorEnabled && !inputLay_senha.isErrorEnabled) {
            if (edit_senha.text.toString() != edit_repete_senha.text.toString()) {
                inputLay_repete_senha.isErrorEnabled = true
                inputLay_repete_senha.error = "As senhas informadas não conferem"
            } else {
                inputLay_repete_senha.isErrorEnabled = false
            }
        }
    }
}

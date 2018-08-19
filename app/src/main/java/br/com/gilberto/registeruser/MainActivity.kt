package br.com.gilberto.registeruser

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import br.com.gilberto.registeruser.model.Pessoa
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var inputLay_nome_completo: TextInputLayout
    private lateinit var inputLay_data_nascimento: TextInputLayout
    private lateinit var inputLay_email: TextInputLayout
    private lateinit var inputLay_senha: TextInputLayout
    private lateinit var inputLay_repete_senha: TextInputLayout

    private lateinit var edit_nome_completo: AppCompatEditText
    private lateinit var edit_email: AppCompatEditText
    private lateinit var edit_senha: AppCompatEditText
    private lateinit var edit_repete_senha: AppCompatEditText
    private lateinit var edit_data_nascimento: AppCompatEditText
    private lateinit var spinner_sexo: AppCompatSpinner
    private lateinit var spinner_profissao: AppCompatSpinner
    private lateinit var spinner_estadoCivil: AppCompatSpinner

    private lateinit var toolbar: Toolbar

    private lateinit var profissoes: ArrayList<String>
    private lateinit var sexos: ArrayList<String>
    private lateinit var estadosCivis: ArrayList<String>

    private lateinit var context: Context

    companion object {
        val activity_result: Int = 99
        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar_include)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        context = this

        profissoes = arrayListOf("Selecione", "## Adicionar Nova ##", "Desenvolvedor", "Analista", "Ator")
        sexos = arrayListOf("Selecine", "Mascunilo", "Feminino")
        estadosCivis = arrayListOf("Selecine", "Solteiro", "Casado", "Divorciado")

        inputLay_nome_completo = findViewById(R.id.til_nome_completo)
        inputLay_data_nascimento = findViewById(R.id.til_data_nascimento)
        inputLay_email = findViewById(R.id.til_email)
        inputLay_repete_senha = findViewById(R.id.til_repeat_senha)
        inputLay_senha = findViewById(R.id.til_senha)

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

        spinner_profissao.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if (position == 1) {
                    //Adicionar nova profissa
                    dialogNovaProfissao()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }

        }

        btn_salvar.setOnClickListener {

            if (validarForm()) {

                val pessoa = popularObjeto()

                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("pessoa", pessoa)
                startActivityForResult(intent, activity_result)
            }
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

    private fun dialogNovaProfissao() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Nova Profissão")
        builder.setCancelable(false)

        val input = EditText(context)
        input.hint = "Informe o noma da nova profissão"
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            profissoes.add(input.text.toString())
            spinner_profissao.setSelection(profissoes.size - 1)
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }

        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            activity_result -> {
                if (resultCode == Activity.RESULT_OK) {
                    val pessoa = data?.getSerializableExtra("PARAM_ACTIVITY2") as Pessoa
                    println("Pessoa retorno: $pessoa")
                    populateForm(pessoa)
                }
            }
        }
    }

    private fun populateForm(pessoa: Pessoa) {
        edit_nome_completo.setText(pessoa.nome)
        edit_email.setText(pessoa.email)
        edit_senha.setText(pessoa.senha)
        edit_repete_senha.setText(pessoa.repeteSenha)
        edit_data_nascimento.setText(format.format(pessoa.nascimento))
        spinner_profissao.setSelection(profissoes.indexOf(pessoa.profissao))
        spinner_estadoCivil.setSelection(estadosCivis.indexOf(pessoa.estadoCivil))
        spinner_sexo.setSelection(sexos.indexOf(pessoa.sexo))

    }

    private fun popularObjeto(): Pessoa {

        return Pessoa(nome = edit_nome_completo.text.toString(),
                nascimento = LocalDate.parse(edit_data_nascimento.text, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)),
                sexo = spinner_sexo.selectedItem.toString(),
                profissao = spinner_profissao.selectedItem.toString(),
                estadoCivil = spinner_estadoCivil.selectedItem.toString(),
                email = edit_email.text.toString(),
                senha = edit_senha.text.toString(),
                repeteSenha = edit_repete_senha.text.toString())
    }

    private fun selectDate() {

        val c = Calendar.getInstance()
        val mYear =
            c.get(Calendar.YEAR)

        val mMonth = c.get(Calendar.MONTH)

        val mDay = c.get(Calendar.DAY_OF_MONTH)



        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var text = ""
                    text += if (dayOfMonth < 10) {
                        "0$dayOfMonth/"
                    } else {
                        "$dayOfMonth/"
                    }
                    text += if (monthOfYear < 10) {
                        "0$monthOfYear/"
                    } else {
                        "$monthOfYear/"
                    }

                    text += "$year"
                    edit_data_nascimento.setText("$text")
                }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {


    }


    private fun populationSpiners() {

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sexos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_sexo.adapter = adapter

        val adapterEstadoCivil = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, estadosCivis)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_estadoCivil.adapter = adapterEstadoCivil

        val adapterProfissao = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, profissoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_profissao.adapter = adapterProfissao
    }

    private fun validarForm(): Boolean {

        var validado = true

        if (edit_nome_completo.text.toString().isEmpty()) {
            inputLay_nome_completo.isErrorEnabled = true
            inputLay_nome_completo.error = "Por favor, preencher o campo nome completo"
            validado = false
        } else {
            inputLay_nome_completo.isErrorEnabled = false
        }

        if (edit_data_nascimento.text.toString().isEmpty()) {
            inputLay_data_nascimento.isErrorEnabled = true
            inputLay_data_nascimento.error = "Por favor, preencher o campo data de nascimento"
            validado = false
        } else {
            inputLay_data_nascimento.isErrorEnabled = false
        }

        if (edit_email.text.toString().isEmpty()) {
            inputLay_email.isErrorEnabled = true
            inputLay_email.error = "Por favor, preencher o campo email"
            validado = false
        } else {
            inputLay_email.isErrorEnabled = false
        }

        if (spinner_sexo.selectedItemPosition == 0) {
            Toast.makeText(context, "Por favor, preencher o campo sexo", Toast.LENGTH_LONG).show()
            validado = false
        }

        if (spinner_profissao.selectedItemPosition == 0 || spinner_profissao.selectedItemPosition == 1) {
            Toast.makeText(context, "Por favor, preencher o campo profissão", Toast.LENGTH_LONG).show()
            validado = false
        }

        if (spinner_estadoCivil.selectedItemPosition == 0) {
            Toast.makeText(context, "Por favor, preencher o campo estado civil", Toast.LENGTH_LONG).show()
            validado = false
        }

        if (edit_senha.text.toString().isEmpty()) {
            inputLay_senha.isErrorEnabled = true
            inputLay_senha.error = "Por favor, preencher o campo senha"
            validado = false
        } else {
            inputLay_senha.isErrorEnabled = false
        }


        if (edit_repete_senha.text.toString().isEmpty()) {
            inputLay_repete_senha.isErrorEnabled = true
            inputLay_repete_senha.error = "Por favor, confirmar a senha"
            validado = false
        } else {
            inputLay_repete_senha.isErrorEnabled = false
        }

        if (!inputLay_senha.isErrorEnabled && !inputLay_senha.isErrorEnabled) {
            if (edit_senha.text.toString() != edit_repete_senha.text.toString()) {
                inputLay_repete_senha.isErrorEnabled = true
                inputLay_repete_senha.error = "As senhas informadas não conferem"
                validado = false
            } else {
                inputLay_repete_senha.isErrorEnabled = false
            }
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches()) {
            inputLay_email.isErrorEnabled = true
            inputLay_email.error = "O email informado é invalido"
            validado = false
        } else {
            inputLay_email.isErrorEnabled = false
        }

        return validado
    }
}

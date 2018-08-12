package br.com.gilberto.registeruser

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import br.com.gilberto.registeruser.model.Pessoa
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var pessoa: Pessoa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        toolbar = findViewById(R.id.toolbar_include)
        setSupportActionBar(toolbar)

        pessoa = intent.getSerializableExtra("pessoa") as Pessoa

        popularForm()

        button_fechar.setOnClickListener {
            onBackPressed()
        }

    }

    private fun popularForm() {

        edit_nome.setText(pessoa.nome)
        edit_email.setText(pessoa.email)
        edit_senha.setText(pessoa.senha)
        edit_sexo.setText(pessoa.sexo)
        edit_profissao.setText(pessoa.profissao)
        edit_estadoCivil.setText(pessoa.estadoCivil)
        edit_nascimento.setText(MainActivity.format.format(pessoa.nascimento))
    }

    override fun onBackPressed() {
        val it = Intent()
        it.putExtra("PARAM_ACTIVITY2", pessoa)
        setResult(RESULT_OK, it)
        finish()
    }
}

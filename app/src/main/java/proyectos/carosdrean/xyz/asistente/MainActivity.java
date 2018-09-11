package proyectos.carosdrean.xyz.asistente;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int RECONOCEDOR_VOZ = 7;
    private TextView escuchando;
    private TextView respuesta,text3;
    private ArrayList<Respuestas> respuest;
    private TextToSpeech leer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);




           escuchando.setText(escuchado);
            String aguja = "pedido";             //palabra buscada
            String pajar = escuchado;    //texto

//escapar y agregar limites de palabra completa - case-insensitive
            Pattern regex = Pattern.compile("\\b" + Pattern.quote(aguja) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher match = regex.matcher(pajar);
            String var2 = escuchado;
            String cantidad = null,mesa = null,productos;

            String[] var4 = var2.split(" ");

            for(int var5 = 0; var5 < var4.length; ++var5) {
                System.out.println(var2);
                if (var4[var5].equals("pedido") && var4[var5 + 1].equals("primera")&&  var4[var5 + 2].equals("mesa")) {
                    String var6 = var4[var5 + 3];
                    if (var4[var5 + 1].equals("primera")){
                        mesa="1";
                    }
                    if (var4[var5 + 1].equals("segunda")){
                        mesa="2";
                    }
                    if (var4[var5 + 3].equals("un") || var4[var5 + 3].equals("1")) {
                        cantidad = "1";
                    } else {
                        cantidad=var4[var5 + 3];

                    }

                    if (var4[var5 + 5].equals("")|| var4[var5 + 5].isEmpty()) {
                        productos=var4[var5 + 4];

                    }else {
                        productos=var4[var5 + 4]+" "+var4[var5 + 5];

                    }


                    text3.setText("entrabdo a pedido" + "mesa "+ mesa + "cantidad"+cantidad+"producto"+productos);

                }

                if (var4[var5].equals("cuenta")) {
                    System.out.println("entrando a cuenta");
                }
            }


  /*
            String aguja1 = "1";             //palabra buscada
            String pajar1 = escuchado;    //texto
            Pattern regex1 = Pattern.compile("\\b" + Pattern.quote(aguja1) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher match1 = regex1.matcher(pajar1);

            if (match1.find()) {  //si se quiere encontrar todas las ocurrencias: cambiar el if por while
                Toast.makeText(getApplication(),"Encontrado1: '" + match1.group()
                        + "' dentro de1 '" + pajar1
                        + "' en la posición1 " + match1.start(),Toast.LENGTH_LONG).show();
                escuchando.setText(escuchado);
            }else{


            }*/

            //prepararRespuesta(escuchado);
        }
    }

    private void prepararRespuesta(String escuchado) {
        String normalizar = Normalizer.normalize(escuchado, Normalizer.Form.NFD);
        String sintilde = normalizar.replaceAll("[^\\p{ASCII}]", "");





        int resultado;
        String respuesta = respuest.get(0).getRespuestas();
        for (int i = 0; i < respuest.size(); i++) {
            resultado = sintilde.toLowerCase().indexOf(respuest.get(i).getCuestion());
            if(resultado != -1){
                respuesta = respuest.get(i).getRespuestas();
            }
        }
        responder(respuesta);
    }

    private void responder(String respuestita) {
        respuesta.setText(respuestita);





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void inicializar(){
        escuchando = (TextView)findViewById(R.id.tvEscuchado);
        respuesta = (TextView)findViewById(R.id.tvRespuesta);
        text3= (TextView)findViewById(R.id.textView3);

        respuest = proveerDatos();
        leer = new TextToSpeech(this, this);
    }

    public void hablar(View v){
        Intent hablar = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        hablar.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        startActivityForResult(hablar, RECONOCEDOR_VOZ);
    }

    public ArrayList<Respuestas> proveerDatos(){
        ArrayList<Respuestas> respuestas = new ArrayList<>();
        respuestas.add(new Respuestas("defecto", "¡Aun no estoy programada para responder eso, lo siento!"));
        respuestas.add(new Respuestas("hola", "hola que tal"));
        respuestas.add(new Respuestas("pedido mesa 1", "dime"));

        respuestas.add(new Respuestas("chiste", "¿Sabes que mi hermano anda en bicicleta desde los 4 años? Mmm, ya debe estar lejos"));
        respuestas.add(new Respuestas("adios", "que descanses"));
        respuestas.add(new Respuestas("como estas", "esperando serte de ayuda"));
        respuestas.add(new Respuestas("nombre", "mis amigos me llaman Mina"));
        return respuestas;
    }

    @Override
    public void onInit(int status) {

    }
}

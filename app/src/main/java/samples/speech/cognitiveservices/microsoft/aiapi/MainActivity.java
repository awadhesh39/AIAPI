package samples.speech.cognitiveservices.microsoft.aiapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIButton;
import ai.api.ui.AIDialog;

public class MainActivity extends AppCompatActivity implements AIListener {

    private AIButton aiButton;
    private AIService aiService;
    private Button aiBtn;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AIConfiguration config = new AIConfiguration(
                "552607573df44510941666587c99cb5c",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiButton = (AIButton) findViewById(R.id.micButton);
        aiBtn = (Button)findViewById(R.id.ai_btn);
        resultTextView = (TextView)findViewById(R.id.txt_view);
        aiButton.initialize(config);

        //AIDialog aiDialog = new AIDialog(this, config);
        //aiDialog.setResultsListener(yourListenerImplementation);
        //aiDialog.showAndListen();

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        aiButton.setResultsListener(new AIButton.AIButtonListener() {

            @Override
            public void onResult(final AIResponse response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Result result = response.getResult();

                        // Get parameters
                        String parameterString = "";
                        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                            }
                        }

                        // Show results in TextView.
                        resultTextView.setText("Query:" + result.getResolvedQuery() +
                                "\nAction: " + result.getAction() +
                                "\nParameters: " + parameterString);
                    }
                });
            }

            @Override
            public void onError(final AIError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(error.toString());
                    }
                });
            }

            @Override
            public void onCancelled() {

            }
        });

        aiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();
            }
        });
    }

    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Result result = response.getResult();

                // Get parameters
                String parameterString = "";
                if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                    for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                        parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                    }
                }

                // Show results in TextView.
                resultTextView.setText("Query:" + result.getResolvedQuery() +
                        "\nAction: " + result.getAction() +
                        "\nParameters: " + parameterString);
            }
        });
    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error.toString());
            }
        });
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}

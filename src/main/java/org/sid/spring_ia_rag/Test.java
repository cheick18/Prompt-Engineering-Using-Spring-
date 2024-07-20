package org.sid.spring_ia_rag;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.List;

public class Test {
    public static void main(String[] args){
        String openaiApiKey = System.getenv("OPENAI_API_KEY");

        // Vérifie si la clé API a été récupérée avec succès
        if (openaiApiKey == null || openaiApiKey.isEmpty()) {
            throw new RuntimeException("La clé API OpenAI n'est pas définie dans les variables d'environnement.");
        }
        OpenAiApi openAiApi= new OpenAiApi((openaiApiKey);
        OpenAiChatModel openAiChatModel= new OpenAiChatModel(openAiApi, OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0F)
                .withMaxTokens(300)
                .build());
        String systemMessageText= """ 
Vous êtes un assistant spécialisé dans le domaine de l'analyse des sentiments. 
Votre tâche est d'extraire à partir d'un commentaire le sentiment sur différents aspects
des ordinateurs achetés par les clients. Les aspects qui nous intéressent sont : l'écran, la souris et le clavier.
Le sentiment peut etre: positive, negative ou neutre.
Le résultat attendu sera au format JSON avec les champs suivants :
  - clavier : le sentiment relatif au clavier
  - souris : le sentiment relatif à la souris
  - écran : le sentiment relatif à l'écran
                """;
        String userInputText= """
                Je ne suis satisfait par laqualité de l'écran, mais le clavier est mauvais alors que la souris est plutot de bonne qualité.
                par ailleur je pense que cet ordinateur consomme beaucoup d'ànergie
                """;

        String userInputText1= """
                Je suis satisfait par laqualité de l'écran, mais le clavier est mauvais alors que la souris la qualité est plutot moyenne.
                par ailleur je pense que cet ordinateur consomme beaucoup d'ànergie
                """;
        UserMessage userMessage1=new UserMessage(userInputText);

        String response1= """
                {
                "clavier":"negative",
                "souris":"neutre",
                "écran":"positive"
                }
                """;
        AssistantMessage assistantMessage1= new AssistantMessage(response1);

        SystemMessage systemMessage= new SystemMessage(systemMessageText);
        UserMessage userMessage=new UserMessage(userInputText);
        Prompt prompt=new Prompt(List.of(assistantMessage1,userMessage));
        ChatResponse chatResponse= openAiChatModel.call(prompt);
        System.out.println(chatResponse.getResult().getOutput().getContent());




    }
}

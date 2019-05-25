package brobot.nagger;

import brobot.ResponseObject;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SheetsQuickstart {
    private final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";
    Map<Date, List<String>> dateToIncompleteTasksMap = new LinkedHashMap<>();
    List<String> currentTasks = new ArrayList<>();

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public SheetsQuickstart() {
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void setup() throws IOException, GeneralSecurityException, ParseException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1k9H4UZ1dPTMlPm9cH4zfK6erQNr6RQltMnn1imjo5PM";
        final String range = "Calendar!A1:N38";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            List<Object> monthRow = values.get(0);
            List<Object> weekHeaderRow = values.get(1);
            // process week by week
            for (int i = 2; i < values.size()-5; i+=6) {
                List<Object> dateRow = values.get(i);
                List<List<Object>> taskRows = new LinkedList<>();
                for (int j = 1; j <= 5; ++j) {
                    taskRows.add(values.get(i+j));
                }

                processTaskRows(dateRow, taskRows);
            }
        }
    }

    public void nag(final ResponseObject responseObject) {
        List<StringBuilder> bldrs = new ArrayList<>();
        for (Map.Entry<Date, List<String>> entry : dateToIncompleteTasksMap.entrySet()) {
            Date date = entry.getKey();
            StringBuilder bldr = new StringBuilder(date.toString()).append("\n");
            for (String task : entry.getValue()) {
                bldr.append("\t").append(task).append("\n");
            }
            if (!bldr.toString().isEmpty()) {
                bldrs.add(bldr);
            }
        }

        if (!bldrs.isEmpty()) {
            responseObject.addMessage("<@128732573495984129>, you have tasks that are incomplete. Please finish them up and mark them as completed.\n");
            for (StringBuilder bldr : bldrs) {
                responseObject.addMessage(bldr.toString());
            }
        } else {
            responseObject.addMessage("Congratulations <@128732573495984129>, you're all caught up!");
        }
    }

    public void displayTasksForToday(final ResponseObject responseObject) {
        responseObject.addMessage("<@128732573495984129>, these are your tasks for today.\n");
        for (String task : currentTasks) {
            responseObject.addMessage("\t" + task + "\n");
        }
    }

    private void processTaskRows(final List<Object> dateRow, final List<List<Object>> taskRows) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        Map<Date, List<String>> dateToTaskMap = new LinkedHashMap<>();
        List<Date> dates = new ArrayList<>();

        for (Object dateObj : dateRow) {
            if (!dateObj.toString().isEmpty()) {
                String dateStr = dateObj + "/2019";
                Date date = dateFormat.parse(dateStr);
                dateToTaskMap.putIfAbsent(date, new ArrayList<>());
                dates.add(date);
            }
        }

        for (List<Object> taskRow : taskRows) {
            for (int j = 0; j < taskRow.size()-1; j+=2) {
                String checkbox = (String) taskRow.get(j);
                String task = (String) taskRow.get(j+1);
                if (!task.isEmpty() && checkbox.equals("FALSE")) {
                    Date dateOfTask = dates.get(j/2);

                    Calendar cal1 = Calendar.getInstance().getInstance();
                    Calendar cal2 = Calendar.getInstance().getInstance();
                    cal1.setTime(dateOfTask);
                    cal2.setTime(currentDate);
                    if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                        currentTasks.add("\t**" + task + "**");
                    } else if (dateOfTask.before(currentDate)) {
                        List<String> tasks = dateToIncompleteTasksMap.getOrDefault(dateOfTask, new ArrayList<>());
                        tasks.add("\t**" + task + "**");
                        dateToIncompleteTasksMap.putIfAbsent(dateOfTask, tasks);
                    } else {
                        dateToTaskMap.get(dateOfTask).add(task);
                    }
                }
            }
        }
    }
}
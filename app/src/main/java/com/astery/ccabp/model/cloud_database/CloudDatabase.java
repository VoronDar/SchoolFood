package com.astery.ccabp.model.cloud_database;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astery.ccabp.model.cloud_database.pogo.GotChild;
import com.astery.ccabp.model.cloud_database.pogo.GotHolyday;
import com.astery.ccabp.model.cloud_database.pogo.GotSchedule;
import com.astery.ccabp.model.cloud_database.pogo.GotSchool;
import com.astery.ccabp.model.cloud_database.pogo.GotTask;
import com.astery.ccabp.model.cloud_database.pogo.PostChild;
import com.astery.ccabp.model.cloud_database.pogo.PostMenu;
import com.astery.ccabp.model.cloud_database.pogo.PostPlanTask;
import com.astery.ccabp.model.cloud_database.pogo.PostTask;
import com.astery.ccabp.model.local_database.ChildDatabase;
import com.astery.ccabp.model.local_database.HolydayDatabase;
import com.astery.ccabp.model.local_database.MenuDatabase;
import com.astery.ccabp.model.local_database.PlanDatabase;
import com.astery.ccabp.model.local_database.ScheduleDatabase;
import com.astery.ccabp.model.local_database.TaskDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.things.Menu;
import com.astery.ccabp.model.things.Scedule;
import com.astery.ccabp.model.things.Task;
import com.astery.ccabp.model.transport_preferences.TPStorage;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.MainFragments.CalendarFragment;
import com.astery.ccabp.view.activities.MainFragments.MenuFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.astery.ccabp.model.cloud_database.CloudUtils.thereIsNoNet;

public class CloudDatabase {
    private final Context context;
    public Loadable loadable;

    private final String DOMEN = "https://schoolfood51.ru";
    private final String API = "Cirv4xUtnNLKql7N9mVlxlXrQ7KpFx61HovEMj99";

    public CloudDatabase(Context context, Loadable loadable) {
        this.context = context;
        this.loadable = loadable;
    }

    public CloudDatabase(Context context) {
        this.context = context;
    }


    public void getPayment(int homeId){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url= DOMEN + "/getPayment/" + API + "/" + homeId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        loadable.onFailure();
                    } else{
                        JSONObject json;
                        try {
                            json = new JSONObject(response);
                            TransportPreferences.setPayment(context, 0, json.getInt("breakfast"));
                            TransportPreferences.setPayment(context, 1, json.getInt("lanch"));
                            TransportPreferences.setPayment(context, 2, json.getInt("snack10"));
                            TransportPreferences.setPayment(context, 3, json.getInt("snack15"));
                            Task.genPayment(json.getInt("breakfast"), json.getInt("lanch"), json.getInt("snack10"), json.getInt("snack15"));
                            loadable.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadable.onError();
                        }
                    }
                }, error ->  {
            thereIsNoNet(context);
            loadable.onError();});


        queue.add(stringRequest);
    }


    public void getSchools(SchoolsGettable gettable){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url= DOMEN + "/getSchools/" + API;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        gettable.onFailure();
                    } else{
                        JSONArray json;
                        try {
                            JSONObject obj = new JSONObject(response);
                            json = obj.getJSONArray("schools");
                            ArrayList<GotSchool> schools = new ArrayList<>();
                            for (int i = 0; i < json.length(); i++){
                                schools.add(new Gson().fromJson(json.get(i).toString(), GotSchool.class));
                            }

                            gettable.onSuccess(schools);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            gettable.onError();
                        }
                    }
                }, error ->  {
            thereIsNoNet(context);
            gettable.onError();});


        queue.add(stringRequest);
    }


    public void getMonthTask(int month, int year, String child, int childPos){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url= DOMEN + "/monthTask/" + API + "/" + "{\"childId\": " +  child + ", \"year\": " + year  + ", \"month\": " + month + "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        loadable.onFailure();
                    } else{

                        JSONObject json;
                        try {
                            json = new JSONObject(response);
                            JSONArray json2 = json.getJSONArray("tasks");

                            ArrayList<Task> tasks = new ArrayList<>();
                            ArrayList<Task> initials = (CalendarFragment.tasks.get(childPos) != null)?CalendarFragment.tasks.get(childPos): new ArrayList<>();
                            upper:
                            for (int i = 0; i < json2.length(); i++){
                                GotTask g = new Gson().fromJson(json2.get(i).toString(), GotTask.class);
                                Task task = new Task("0", child, g.getDay() ,g.isBreakfast(), g.isLanch(), g.isSnack10(), g.isSnack15(), g.getEnable(), g.getTask(), true);
                                for (Task t: initials){
                                    if (t.getDate().equals(task.getDate())) {
                                        tasks.add(t);
                                        continue upper;
                                    }
                                }
                                task.setAccepted(true);
                                tasks.add(task);
                            }
                            CalendarFragment.tasks.set(childPos, tasks);

                            TaskDatabase db = TaskDatabase.getInstance(context);
                            db.open();
                            for (Task task: tasks)
                                db.update(task);
                            /*
                            Log.i("main", "=====================");
                            for (Task task: db.getEverything())
                                Log.i("main", task.toString());
                            db.close();

                             */

                            loadable.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadable.onFailure();
                        }
                    }
                }, error ->  {
            thereIsNoNet(context);
            loadable.onError();});


        queue.add(stringRequest);
    }



    public void getMonthHolyday(int month, int year, String child, int childPos){
        Log.i("main", "start");
        RequestQueue queue = Volley.newRequestQueue(context);
        String url= DOMEN + "/monthHolydays/" + API + "/" + "{\"childId\": " +  child + ", \"year\": " + year  + ", \"month\": " + month + "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        loadable.onFailure();
                    } else{
                        Log.i("main", response);

                        JSONObject json;
                        try {
                            json = new JSONObject(response);
                            JSONArray json2 = json.getJSONArray("holydays");

                            HolydayDatabase db = HolydayDatabase.getInstance(context);
                            db.open();
                            ArrayList<GotHolyday> holydays = new ArrayList<>();
                            for (int i = 0; i < json2.length(); i++){
                                GotHolyday g = new Gson().fromJson(json2.get(i).toString(), GotHolyday.class);
                                holydays.add(g);
                                g.setMonth(GotHolyday.generateMonth(year, month));
                                g.setChildId(Integer.parseInt(child));
                                db.update(g);
                            }
                            db.close();
                            CalendarFragment.holydays.set(childPos, holydays);
                            loadable.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadable.onFailure();
                        }
                    }
                }, error ->  {
            thereIsNoNet(context);
            loadable.onError();});


        queue.add(stringRequest);
    }

    public void getTask(TaskGettable gettable, int day, String child, int classId, int weekDay){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url= DOMEN + "/tomorrowTask/" + API + "/" + "{\"childId\": " +  child + ", \"classId\": " + classId + "}";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        gettable.onFailure();
                    } else{
                        GotTask gT = new Gson().fromJson(response, GotTask.class);
                        Task t = new Task(child, child, gT.getDay(), gT.isBreakfast(), gT.isLanch(), gT.isSnack10(), gT.isSnack15(), classId);
                        TransportPreferences.setTodayTaskId(context, t.getId(), t.getTimeStamp(), child);
                        gettable.onSuccess(t);
                        TaskDatabase db = TaskDatabase.getInstance(context);
                        db.open();
                        t.setAccepted(true);
                        t.setTask(true);
                        db.update(t);
                        db.close();
                    }
                }, error ->  {
                thereIsNoNet(context);
                gettable.onError();});


        queue.add(stringRequest);
    }

    public void getLastTask(String day, String child, LastGettable gettable){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url= DOMEN + "/lastTask/" + API + "/"  +  child + "/" + day;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        gettable.onFailure();
                    } else{
                        GotTask gT = new Gson().fromJson(response, GotTask.class);
                        ArrayList<String> ar= new ArrayList<>();
                        ar.add((gT.isBreakfast())?"Да":"Нет");
                        ar.add((gT.isLanch())?"Да":"Нет");
                        ar.add((gT.isSnack10())?"Да":"Нет");
                        ar.add((gT.isSnack15())?"Да":"Нет");
                        gettable.onSuccess(ar);
                    }
                }, error ->  {
            thereIsNoNet(context);});


        queue.add(stringRequest);
    }

    public void getPlan(PlanGettable gettable, String childId){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url= DOMEN + "/plan/" + API + "/" + childId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){

                        gettable.onFailure();
                    } else{

                        JSONObject json;
                        try {
                            json = new JSONObject(response);
                            JSONArray json2 = json.getJSONArray("plan");
                            try {
                                ArrayList<Task> taskes = new ArrayList<>();
                                PlanDatabase db = PlanDatabase.getInstance(context);
                                db.open();
                                for (int i = 0; i < 6; i++){
                                    PostPlanTask g = new Gson().fromJson(json2.get(i).toString(), PostPlanTask.class);
                                    Task task = new Task(g.getChildId() + "", g.getDay()+ "", g.isBreakfast(), g.isLanch(), g.isSnack10(), g.isSnack15(), g.getClassId());
                                    taskes.add(task);
                                    db.update(task);
                                    TPStorage.loadPlanTask(context, task, Integer.parseInt(task.getTimeStamp()));
                                    TPStorage.saveTemperPlan(context, task, Integer.parseInt(task.getTimeStamp()));
                                }
                                db.close();
                                gettable.onSuccess(taskes);
                            } catch (IndexOutOfBoundsException e){
                                gettable.onFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            gettable.onFailure();
                        }

                    }
                }, error ->  {
                thereIsNoNet(context);
                gettable.onError();});


        queue.add(stringRequest);
    }


    public void loadPlan(ArrayList<Task> taskes, int classId){
        StringBuilder json = new StringBuilder("{ \"plan\" : [");
        for (Task task: taskes) {
            TPStorage.loadPlanTask(context, task, Integer.parseInt(task.getTimeStamp()));
            TPStorage.saveTemperPlan(context, task, Integer.parseInt(task.getTimeStamp()));
            json.append(new Gson().toJson(new PostPlanTask(Integer.parseInt(task.getTimeStamp()),
                    Integer.parseInt(task.getChildId()), classId,
                    task.isBreakfast(), task.isLanch(), task.isSnak10(), task.isSnak15())));
            json.append(", ");
        }
        json.delete(json.length()-2,json.length()-1);
        json.append("]}");



        RequestQueue queue = Volley.newRequestQueue(context);
        String url=DOMEN + "/plan/" + API + "/" + json;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("f")){
                        loadable.onFailure();
                    } else{
                        loadable.onSuccess();
                    }
                }, error ->  {
            thereIsNoNet(context);
            loadable.onError();});


        queue.add(stringRequest);

    }


    public void loadTask(Task task){

        PostTask pTask = new PostTask(task.getTimeStamp(), Integer.parseInt(task.getChildId()), task.getGroup(), task.isBreakfast(), task.isLanch(), task.isSnak10(), task.isSnak15());

        String json = new Gson().toJson(pTask);

        TPStorage.loadTodayTask(context, task);
        TPStorage.saveTemperTask(context, task);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url=DOMEN + "/tomorrowTask/" + API + "/" + json;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("f")){
                        loadable.onFailure();
                    } else{
                        task.setId(response);
                        TransportPreferences.setTodayTaskId(context, response, task.getTimeStamp(), task.getChildId());
                        loadable.onSuccess();
                    }
                }, error ->  {
            thereIsNoNet(context);
            loadable.onError();});


        queue.add(stringRequest);

    }
    static String  prepare(String word){
        if(word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public void addChild(Child child, int parentId, ALoadable load){

        if (child.getName() != null)
            child.setName(prepare(child.getName()));
        if (child.getsName() != null)
            child.setsName(prepare(child.getsName()));
        if (child.getpName() != null)
            child.setpName(prepare(child.getpName()));

        PostChild pChild = new PostChild(parentId, child.getName(), child.getsName(), child.getSchool(), child.getGrade(), child.isFree(), child.getClassCode());
        pChild.setPatronymic(child.getpName());
        String json = new Gson().toJson(pChild);


        RequestQueue queue = Volley.newRequestQueue(context);
        String url=DOMEN + "/child/" + API + "/" + json;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject json1;
                    Log.i("flask", response);
                    switch (response) {
                        case "f":
                        case "ic":
                        case "n":
                        case "p":
                        case "e":
                            load.onFailure(response);
                            return;
                    }

                    try {
                        json1 = new JSONObject(response);
                        child.setClassId(json1.getInt("classId"));
                        child.setHomeId(json1.getInt("homeId"));
                        TransportPreferences.setDaysInCycle(context, child.getHomeId(), json1.getInt("daysInCycle"));
                        child.setActivated(true);
                        ChildDatabase.getInstance(context).reUpdate(child, json1.getString("id"));
                        load.onSuccess();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error ->  {
                    thereIsNoNet(context);
                load.onError();});

        queue.add(stringRequest);

    }

    public void updateChildName(Child child){
        String json = "{\"id\":\"" + child.getId() + "\", \"name\":\"" + child.getName()
                + "\", \"lastName\":\"" + child.getsName() + "\"}";


        RequestQueue queue = Volley.newRequestQueue(context);
        String url=DOMEN + "/child/" + API + "/" + json;
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if (response.equals("f")) loadable.onFailure();
                    else{
                        loadable.onSuccess();}
                }, error ->  {
            thereIsNoNet(context);
            loadable.onError();});

        queue.add(stringRequest);

    }

    public void deleteChild(String childId){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url=DOMEN + "/child/" + childId;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    if (response.equals("f")) loadable.onFailure();
                    else loadable.onSuccess();
                }, error ->  {
                    thereIsNoNet(context);
                    loadable.onError();
        });

        queue.add(stringRequest);
    }


    public void downloadChildren(String parentId){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url=DOMEN + "/child/" + API + "/" + parentId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")) loadable.onFailure();
                    else{
                        ChildDatabase db = ChildDatabase.getInstance(context);

                        JSONObject json;
                        try {
                            json = new JSONObject(response);
                            JSONArray json2 = json.getJSONArray("children");
                                for (int i = 0; i < json2.length(); i++) {
                                    GotChild g = new Gson().fromJson(json2.get(i).toString(), GotChild.class);
                                    TransportPreferences.setDaysInCycle(context, g.getHomeId(), g.getDaysInCycle());
                                    TransportPreferences.setStartCycle(context, g.getHomeId(), g.getStartCycle());
                                    db.update(new Child(g.getId(), g.getName(), g.getLastName(), g.getSchoolId(), g.getClassName(), g.getClassId(), g.isAvailable(), g.getSchoolName(), g.getHomeId(), g.getPatronymic(), g.isFree()));
                                }
                                loadable.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadable.onSuccess();
                        }
                    }
                }, error -> {
            thereIsNoNet(context);
            loadable.onError();
        });
        queue.add(stringRequest);
    }

    /** возвращает меню по конкретному дню*/
    public void getMenu(int homeId, String day, MenuGettable menuGettable){
        String url=DOMEN + "/getMenuToday/"+ API + "/" + homeId +"/" + day;
        getMenu(url, menuGettable, true, -1, -1, homeId);

    }

    /** возвращает меню по дню недели + неделе*/
    public void getMenu(int homeId, int weekday, int week, MenuGettable menuGettable){
        String url=DOMEN + "/getMenu/"+ API + "/" + homeId +"/" + weekday + "/" + week;
        getMenu(url, menuGettable, false, weekday, week, homeId);
    }

    private void getMenu(String url, MenuGettable menuGettable, boolean updateWeek, int weekday, int week, int homeId){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        menuGettable.onFailure();
                        return;
                    }
                    ArrayList<Menu> menu;
                    PostMenu m = new Gson().fromJson(response, PostMenu.class);
                    m.setYoung(false);
                    m.setHomeId(homeId);
                    if (week != -1){
                        m.setWeek(week);
                        m.setWeekday(weekday);
                    }
                    MenuDatabase db = MenuDatabase.getInstance(context);
                    db.update(m);
                    menu = Menu.convertFromPost(m);

                    if (updateWeek) {
                        MenuFragment.getWeek = m.getWeek();
                        MenuFragment.getWeekDay = m.getWeekday();

                    }

                    menuGettable.onSuccess(menu);

                }, error ->  {
            //thereIsNoNet(context);
            menuGettable.onFailure();});

        queue.add(stringRequest);
    }


    /** возвращает строку индекса, если она есть или null, если по индексу ничего нет*/
    private String s(List<String> list, int index){
        if (list.size() <= index) return null;
        return list.get(index);
    }



    public void getAvailables(ArrayList<Child> children){

        StringBuilder st = new StringBuilder("{\"ids\" : [");
        for (Child child: children){
            st.append(child.getId()).append(", ");
        }
        st.delete(st.length()-2,st.length()-1);
        st.append("]}");


        String url=DOMEN + "/isChildAccept/"+ API + "/" + st.toString();
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JSONObject json;
                    try {
                        json = new JSONObject(response);
                        JSONArray json2 = json.getJSONArray("acess");
                        for (int i =0; i < children.size(); i++) {
                            boolean maybe = json2.getBoolean(i);
                            if (children.get(i).isActivated() != maybe) {
                                children.get(i).setActivated(maybe);
                                ChildDatabase.getInstance(context).update(children.get(i));
                            }
                        }
                        loadable.onSuccess();

                    } catch (JSONException e) {
                        if (e.getMessage() != null)
                        loadable.onSuccess();
                    }

                }, error -> {
                    thereIsNoNet(context);
                    loadable.onError();}
                    );

        queue.add(stringRequest);

    }


    public void getScedule(String group, SceduleGettable gettable){

        String url=DOMEN + "/showSchedule/"+ API + "/" + group;
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")){
                        gettable.onFailure();
                        return;
                    }
                    GotSchedule sc = new Gson().fromJson(response, GotSchedule.class);
                    ScheduleDatabase db = ScheduleDatabase.getInstance(context);
                    db.update(sc, Integer.parseInt(group));
                    db.close();
                    ArrayList<Scedule> scedules = GotSchedule.convertFromPost(sc);
                    gettable.onSuccess(scedules);

                }, error -> {
                    thereIsNoNet(context);
                    gettable.onFailure();});

        queue.add(stringRequest);
    }



    public interface Loadable{
        void onFailure();
        void onSuccess();
        void onError();
    }


    public interface ALoadable{
        void onFailure(String message);
        void onSuccess();
        void onError();
    }

    public interface TaskGettable{
        void onSuccess(Task task);
        void onError();
        void onFailure();
    }


    public interface MenuGettable{
        void onFailure();
        void onSuccess(ArrayList<Menu> menus);
    }

    public interface SceduleGettable{
        void onFailure();
        void onSuccess(ArrayList<Scedule> menus);
    }
    public interface LastGettable{
        void onFailure();
        void onSuccess(ArrayList<String> things);
    }

    public interface PlanGettable{
        void onFailure();
        void onError();
        void onSuccess(ArrayList<Task> plan);
    }
    public interface SchoolsGettable{
        void onFailure();
        void onError();
        void onSuccess(ArrayList<GotSchool> schools);
    }
}

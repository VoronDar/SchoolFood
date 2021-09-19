package com.astery.ccabp.model.transport_preferences;

import android.content.Context;

/**
 * отвечает за получение/исправление данных в transportPreference
 */
public class TransportPreferences {

    private static final String PREFERENCE_NAME = "preference";

    //// Базовые функции для упрощения /////////////////////////////////////////////////////////////
    private static void setPref(Context context, String pref, int value){
        context.getSharedPreferences(PREFERENCE_NAME, 0).edit().
                putInt(pref, value).apply();
    }
    private static void setPref(Context context, String pref, boolean value){
        context.getSharedPreferences(PREFERENCE_NAME, 0).edit().
                putBoolean(pref, value).apply();
    }
    private static void setPref(Context context, String pref, String value){
        context.getSharedPreferences(PREFERENCE_NAME, 0).edit().
                putString(pref, value).apply();
    }
    private static int getInt(Context context, String pref){
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getInt(pref, 0);
    }
    private static boolean getBoolean(Context context, String pref){
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getBoolean(pref, false);
    }
    private static String getString(Context context, String pref){
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString(pref, "");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //// REGISTER PREFERENCES //////////////////////////////////////////////////////////////////////
    /** если последний вход был входом, то ставится ложно.
     * Нужно для того, чтобы подавать сохраненные значения в полях регистрации */
    public static void setSigned(Context context, Boolean value){
        setPref(context, "signed", value);
    }
    public static Boolean isSigned(Context context){
        return getBoolean(context, "signed");
    }

    public static void setName(Context context, String value){
        setPref(context, "name", value);
    }
    public static String getName(Context context){
        return getString(context, "name");
    }
    public static void setSecondName(Context context, String value){
        setPref(context, "s_name", value);
    }
    public static String getSecondName(Context context){
        return getString(context, "s_name");
    }

    public static void setPassword(Context context, String value){
        setPref(context, "password", value);
    }
    public static String getPassword(Context context){
        return getString(context, "password");
    }
    public static void setConfirmPassword(Context context, String value){
        setPref(context, "c_password", value);
    }
    public static String getConfirmPassword(Context context){
        return getString(context, "c_password");
    }

    public static void setEmail(Context context, String value){
        setPref(context, "email", value);
    }
    public static String getEmail(Context context){
        return getString(context, "email");
    }
    public static void setPhone(Context context, String value){
        setPref(context, "phone", value);
    }
    public static String getPhone(Context context) {
        return getString(context, "phone");
    }
    public static void setParentId(Context context, int value) {
        setPref(context, "parentId", value);
    }
    public static int getParentId(Context context) {
        return getInt(context, "parentId");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //// MAIN PREFERENCES //////////////////////////////////////////////////////////////////////////

    /** показывалась ли помощь пользоваьелю */
    public static void setInfoState(Context context, boolean value, int block){
        setPref(context, block + "_info", value);
    }
    public static Boolean getInfoState(Context context, int block) {
        return getBoolean(context, block + "_info");
    }


    /** выбранные пункты питания */
    public static void setTodayCheck(Context context, boolean value, int block, String childId){
        setPref(context, block + "_day_plan" + childId, value);
    }
    public static Boolean getTodayCheck(Context context, int block, String childId) {
        return getBoolean(context, block + "_day_plan" + childId);
    }

    /** ID последней заполненной заявки на питание (сохраняется с днем) */
    public static void setTodayTaskId(Context context, String id, String day, String childId){
        setPref(context, "day_plan_id" + childId, day + "|" + id);
    }
    public static String getTodayTaskId(Context context, String childId){
        return getString(context, "day_plan_id" + childId);
    }

    /** выбранные пункты питания */
    public static void setTodaySaveCheck(Context context, boolean value, int block, String childId){
        setPref(context, block + "_day_save_plan" + childId, value);
    }
    public static Boolean getTodaySaveCheck(Context context, int block, String childId) {
        return getBoolean(context, block + "_day_save_plan" + childId);
    }

    /** принято ли сегодняшнее питание */
    public static void setTodayAccepted(Context context, boolean value, String childId){
        setPref(context, "accepted" + childId, value);
    }
    public static Boolean isTodayAccepted(Context context, String childId) {
        return getBoolean(context, "accepted" + childId);
    }

    /** принято ли сегодняшнее питание */
    public static void setLastDayPlan(Context context, int value, String childId){
        setPref(context, "last_day" + childId, value);
    }
    public static int getLastDayPlan(Context context, String childId) {
        return getInt(context, "last_day" + childId);
    }

    /** выбранные пункты питания по плану */
    public static void setPlanAccept(Context context, boolean value, int day, String childId, int block){
        setPref(context, day + "_plan_accept" + block + "_" + childId, value);
    }
    public static Boolean getPlanAccept(Context context, int day, String childId, int block) {
        return getBoolean(context, day + "_plan_accept" + block + "_" + childId);
    }


    /** стоимость питания */
    public static void setPayment(Context context, int block, int value){
        setPref(context, "payment_" + block, value);
    }
    public static Integer getPayment(Context context, int block) {
        return getInt(context, "payment_" + block);
    }

    /** дней в цикле homeId */
    public static void setDaysInCycle(Context context, int block, int value){
        setPref(context, "days_in_cycle_" + block, value);
    }
    public static Integer getDaysInCycle(Context context, int block) {
        return getInt(context, "days_in_cycle_" + block);
    }
    /** дней в цикле homeId */
    public static void setStartCycle(Context context, int block, String value){
        setPref(context, "start_cycle" + block, value);
    }
    public static String getStartCycle(Context context, int block) {
        return getString(context, "start_cycle" + block);
    }

    /** последний homeId по которому просили оплату */
    public static void setlastPaymentHomeId(Context context, int value){
        setPref(context, "lastPayment", value);
    }
    public static Integer getlastPaymentHomeId(Context context) {
        return getInt(context, "lastPayment");
    }

    /** выбранные пункты питания по плану - сохранение которое живет до выруба активности */
    public static void setPlanSavedAccept(Context context, boolean value, int day, String childId, int block){
        setPref(context, day + "_plan_save_accept" + block + "_" + childId, value);
    }
    public static Boolean getPlanSavedAccept(Context context, int day, String childId, int block) {
        return getBoolean(context, day + "_plan_save_accept" + block + "_" + childId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}

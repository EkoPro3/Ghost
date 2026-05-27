package akro.ghost.tele.Configs;

import android.content.Context;

import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.Clients.Telegraph;
import akro.ghost.tele.features.DisableChannelSwipeBack;
import akro.ghost.tele.features.DisableNumberRounding;
import akro.ghost.tele.features.DisableProfileSwipeBack;
import akro.ghost.tele.features.DisableStories;
import akro.ghost.tele.features.DownloadSpeed;
import akro.ghost.tele.features.EnableSavingStories;
import akro.ghost.tele.features.FixTLError;
import akro.ghost.tele.features.GhostMode;
import akro.ghost.tele.features.HidePhone;
import akro.ghost.tele.features.HidePinnedMessages;
import akro.ghost.tele.features.HideProxySponsor;
import akro.ghost.tele.features.HideUpdateApp;
import akro.ghost.tele.features.HijriDate;
import akro.ghost.tele.features.PreventMedia;
import akro.ghost.tele.features.RemovesContentSaving;
import akro.ghost.tele.features.SaveEditsHistory;
import akro.ghost.tele.features.SecretMediaSave;
import akro.ghost.tele.features.ShowDeletedMessages;
import akro.ghost.tele.features.TelePremium;
import akro.ghost.tele.features.otherFeatures.AlwaysSaveMedia;
import akro.ghost.tele.features.otherFeatures.CopyNameHook;
import akro.ghost.tele.features.otherFeatures.EditOnlineTextView;
import akro.ghost.tele.features.otherFeatures.FeatureInitializer;
import akro.ghost.tele.language.Keys;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.virtuals.ui.Cells.ChatMessageCell;

import java.util.ArrayList;
import java.util.List;


public class ConfigManager {
    

    private static final List<ConfigItem> items = new ArrayList<>();


    // GhostMode
    public static ConfigItem ghostModeSettings;
    public static ConfigItem hideSeen;
    public static ConfigItem markReadAfterSend;
    public static ConfigItem hideTyping;
    public static ConfigItem hideStoryView;
    public static ConfigItem hidePhone;
    public static ConfigItem hideOnline;
    public static ConfigItem onlineInfo;

    public static ConfigItem shadows;

    // Stories
    public static ConfigItem stories;
    public static ConfigItem disableStories;

    // Messages
    public static ConfigItem messages;
    public static ConfigItem showDeletedMessages;
    public static ConfigItem showMessageId;
    public static ConfigItem saveEditsHistory;

    // Connections
    public static ConfigItem connections;
    public static ConfigItem downloadSpeed;

    // Media
    public static ConfigItem media;
    public static ConfigItem secretMediaSave;
    public static ConfigItem preventMedia;
    public static ConfigItem enableSavingStories;

    //UI
    public static ConfigItem ui;
    public static ConfigItem hidePinnedMessages;
    public static ConfigItem disableChannelSwipeBack;
    public static ConfigItem disableProfileSwipeBack;
    public static ConfigItem hideProxySponsor;
    public static ConfigItem customCalendar;

    // Other Features
    public static ConfigItem otherFeatures;
    public static ConfigItem removesContentSaving;
    public static ConfigItem telegramPremium;
    public static ConfigItem disableNumberRounding;
    public static ConfigItem hideUpdateApp;
    public static ConfigItem fixTLError;

    // Button
    public static ConfigItem btnChannel;
    public static ConfigItem btnRestartApp;

    public static void loadAndRead(Context context){
        ConfigPreferences.init();
        load(context);
        readFeature(context);
    }

    public static void load(Context context) {
        // GhostMode
        ghostModeSettings = new ConfigItem(ConfigItem.HEADER, Keys.GhostModeSettings);
        items.add(ghostModeSettings);

        hideSeen = new ConfigItem(ConfigItem.SWITCH, Keys.HideSeen, ConfigPreferences.getBoolean(Keys.HideSeen), GhostMode::init);
        items.add(hideSeen);

        markReadAfterSend = new ConfigItem(ConfigItem.SWITCH, Keys.MarkReadAfterSend, ConfigPreferences.getBoolean(Keys.MarkReadAfterSend), GhostMode::init);
        items.add(markReadAfterSend);

        hideTyping = new ConfigItem(ConfigItem.SWITCH, Keys.HideTyping, ConfigPreferences.getBoolean(Keys.HideTyping), GhostMode::init);
        items.add(hideTyping);

        hideStoryView = new ConfigItem(ConfigItem.SWITCH, Keys.HideStoryView, ConfigPreferences.getBoolean(Keys.HideStoryView), GhostMode::init);
        items.add(hideStoryView);

        hidePhone = new ConfigItem(ConfigItem.SWITCH, Keys.HidePhone, true, ConfigPreferences.getBoolean(Keys.HidePhone), HidePhone::init);
        items.add(hidePhone);

        hideOnline = new ConfigItem(ConfigItem.SWITCH, Keys.HideOnline, true, ConfigPreferences.getBoolean(Keys.HideOnline), GhostMode::init);
        items.add(hideOnline);

        onlineInfo = new ConfigItem(ConfigItem.INFO, Keys.OfflineVisibilityInfo);
        items.add(onlineInfo);

        shadows = new ConfigItem(ConfigItem.DIVIDER);
        items.add(shadows);
        if (!ClientChecker.check(ClientChecker.ClientType.Telegraph)) {

            // Stories
            stories = new ConfigItem(ConfigItem.HEADER, Keys.StoriesSettings);
            items.add(stories);

            disableStories = new ConfigItem(ConfigItem.SWITCH, Keys.DisableStories, true, ConfigPreferences.getBoolean(Keys.DisableStories), DisableStories::init);
            items.add(disableStories);

            items.add(shadows);

            // Messages
            messages = new ConfigItem(ConfigItem.HEADER, Keys.MessagesSettings);
            items.add(messages);

            showDeletedMessages = new ConfigItem(ConfigItem.SWITCH, Keys.ShowDeletedMessages, ConfigPreferences.getBoolean(Keys.ShowDeletedMessages), ShowDeletedMessages::initProcessing);
            items.add(showDeletedMessages);

            if (!ClientChecker.check(ClientChecker.ClientType.NagramX)) {
                showMessageId = new ConfigItem(ConfigItem.SWITCH, Keys.ShowMessageID, ConfigPreferences.getBoolean(Keys.ShowMessageID), ChatMessageCell::init);
                items.add(showMessageId);
            }

            saveEditsHistory = new ConfigItem(ConfigItem.SWITCH, Keys.SaveEditsHistory, ConfigPreferences.getBoolean(Keys.SaveEditsHistory), () -> SaveEditsHistory.init(context));
            items.add(saveEditsHistory);

            items.add(shadows);

            // Connections
            connections = new ConfigItem(ConfigItem.HEADER, Keys.ConnectionsSettings);
            items.add(connections);

            downloadSpeed = new ConfigItem(ConfigItem.SWITCH, Keys.DownloadSpeed, ConfigPreferences.getBoolean(Keys.DownloadSpeed), DownloadSpeed::init);
            items.add(downloadSpeed);

            items.add(shadows);

            // Media
            media = new ConfigItem(ConfigItem.HEADER, Keys.MediaSettings);
            items.add(media);

            secretMediaSave = new ConfigItem(ConfigItem.SWITCH, Keys.SecretMediaSave, ConfigPreferences.getBoolean(Keys.SecretMediaSave), SecretMediaSave::init);
            items.add(secretMediaSave);

            preventMedia = new ConfigItem(ConfigItem.SWITCH, Keys.PreventMedia, ConfigPreferences.getBoolean(Keys.PreventMedia), PreventMedia::init);
            items.add(preventMedia);

            enableSavingStories = new ConfigItem(ConfigItem.SWITCH, Keys.EnableSavingStories, ConfigPreferences.getBoolean(Keys.EnableSavingStories), EnableSavingStories::init);
            items.add(enableSavingStories);

            items.add(shadows);
        }

            // UI
            ui = new ConfigItem(ConfigItem.HEADER, Keys.UiSettings);
            items.add(ui);
        if (!ClientChecker.check(ClientChecker.ClientType.Telegraph)) {

            hidePinnedMessages = new ConfigItem(ConfigItem.SWITCH, Keys.HidePinnedMessages, ConfigPreferences.getBoolean(Keys.HidePinnedMessages), HidePinnedMessages::init);
            items.add(hidePinnedMessages);

            disableChannelSwipeBack = new ConfigItem(ConfigItem.SWITCH, Keys.DisableChannelSwipeBack, ConfigPreferences.getBoolean(Keys.DisableChannelSwipeBack), DisableChannelSwipeBack::init);
            items.add(disableChannelSwipeBack);

            disableProfileSwipeBack = new ConfigItem(ConfigItem.SWITCH, Keys.DisableProfileSwipeBack, ConfigPreferences.getBoolean(Keys.DisableProfileSwipeBack), DisableProfileSwipeBack::init);
            items.add(disableProfileSwipeBack);

            hideProxySponsor = new ConfigItem(ConfigItem.SWITCH, Keys.HideProxySponsor, true, ConfigPreferences.getBoolean(Keys.HideProxySponsor), HideProxySponsor::init);
            items.add(hideProxySponsor);

            customCalendar = new ConfigItem(ConfigItem.TEXT, Keys.Calendar, true, HijriDate::init);
            items.add(customCalendar);

            items.add(shadows);

            // Other Features
            otherFeatures = new ConfigItem(ConfigItem.HEADER, Keys.OtherFeaturesSettings);
            items.add(otherFeatures);

            removesContentSaving = new ConfigItem(ConfigItem.SWITCH, Keys.RemovesContentSaving, ConfigPreferences.getBoolean(Keys.RemovesContentSaving), RemovesContentSaving::init);
            items.add(removesContentSaving);
        }

        telegramPremium = new ConfigItem(ConfigItem.SWITCH, Keys.TelegramPremium, ConfigPreferences.getBoolean(Keys.TelegramPremium), TelePremium::init);
        items.add(telegramPremium);

        if (!ClientChecker.check(ClientChecker.ClientType.Telegraph)) {
            disableNumberRounding = new ConfigItem(ConfigItem.SWITCH, Keys.DisableNumberRounding, "5.3K -> 5300", ConfigPreferences.getBoolean(Keys.DisableNumberRounding), DisableNumberRounding::init);
            hideUpdateApp = new ConfigItem(ConfigItem.SWITCH, Keys.HideUpdateApp, true, ConfigPreferences.getBoolean(Keys.HideUpdateApp), HideUpdateApp::init);
            fixTLError = new ConfigItem(ConfigItem.SWITCH, Keys.FixTLError, ConfigPreferences.getBoolean(Keys.FixTLError), FixTLError::init);
            items.add(disableNumberRounding);
            items.add(hideUpdateApp);
            items.add(fixTLError);
        }

        items.add(shadows);

        btnChannel = new ConfigItem(ConfigItem.TEXT, Keys.DeveloperChannel);
        items.add(btnChannel);

        items.add(shadows);

        btnRestartApp = new ConfigItem(ConfigItem.TEXT, Keys.RestartApp);
        items.add(btnRestartApp);

        items.add(shadows);
    }

    public static List<ConfigItem> getItems() {
        return items;
    }

    public static void readFeature(Context context) {
        try {
            for (ConfigItem item : items) {
                if (item == null) continue;
                if (item.getType() != ConfigItem.SWITCH && item.getCustomCalendar() == 0) continue;
                if (item.isEnable()) item.run();
            }

            if (!ClientChecker.check(ClientChecker.ClientType.Telegraph)) {
                FeatureInitializer.init(context);
                CopyNameHook.init(context);
                AlwaysSaveMedia.init();
                if (!ClientChecker.check(ClientChecker.ClientType.Nagram) && !ClientChecker.check(ClientChecker.ClientType.NagramX)) EditOnlineTextView.init(context);
            } else {
                Telegraph.removeAd();
            }

        } catch (Throwable e) {
            Logger.e(e);
        }
    }

    public static boolean isGhostMode(){
        return hideSeen.isEnable() ||
                hideStoryView.isEnable() ||
                hideTyping.isEnable() ||
                hideOnline.isEnable() ||
                markReadAfterSend.isEnable();

    }

}

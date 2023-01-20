package com.shoptqk.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoptqk.common.entity.setting.Setting;
import com.shoptqk.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	@Autowired private SettingRepository settingRepository;
		
	public List<Setting> getGeneralSettings() {
		return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));
		
		return new EmailSettingBag(settings);
	}
	
	public CurrencySettingBag getCurrencySettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		return new CurrencySettingBag(settings);
	}
}

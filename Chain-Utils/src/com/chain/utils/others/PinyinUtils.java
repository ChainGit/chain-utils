package com.chain.utils.others;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsException;
import com.chain.logging.ChainUtilsLoggerFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类<br>
 * 
 * 依赖pinyin4j
 * 
 * @author Collected
 * @version 1.0
 *
 */
public class PinyinUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(PinyinUtils.class);

	/**
	 * 获得汉字的拼音
	 * 
	 * @param chines
	 *            汉字（字符串）
	 * @return 拼音字母
	 * @throws ChainUtilsException
	 *             拼音转换错误
	 */
	public static String getPinyin(String chines) throws ChainUtilsException {
		String pinyinName = null;
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error("pinyin convert exception " + chines, e);
					throw new ChainUtilsException("pinyin convert exception " + chines, e);
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

}

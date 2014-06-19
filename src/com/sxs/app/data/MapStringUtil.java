package com.sxs.app.data;

import java.io.UnsupportedEncodingException;

import android.util.Log;
public class MapStringUtil {
	static final int GB_SP_DIFF=160;
	// ��Ź���һ�����ֲ�ͬ��������ʼ��λ�� 
    static final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 
    	3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 
        5249, 5600 }; 
    // ��Ź���һ�����ֲ�ͬ��������ʼ��λ���Ӧ���� 
    static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z' }; 
	public static String getStr(Object str){
		
		if(str != null)
		{
			return str.toString();
		}
		return "";
	}
	public static int stringFind(String source, String target){
       int number = 0;
       int i = 0;
       while((i=source.indexOf(target, i))!=-1) {
           number++;
           i++;
       }
       return number;
    }
	
	
	public static String getZiMu(String title)
	{
		String spells = getSpells(title);
		String res = "Z";
		if(spells != null && spells.length() > 0)
		{
			res = spells.substring(0, 1).toUpperCase();
			if(res.equals("-"))
			{
				res = "*";
			}
		}
		return res;
	}
	public static String getSpells(String characters){
    	StringBuffer buffer = new StringBuffer();
    	for(int i=0;i<characters.length();i++){
    		
    		char ch = characters.charAt(i);
    		if((ch>>7)==0){
    			//�ж��Ƿ�Ϊ���֣��������7ΪΪ0�Ͳ��Ǻ��֣������Ǻ���
    		}else{
			char spell = getFirstLetter(ch);
			buffer.append(String.valueOf(spell));
    		}
    	}
		return buffer.toString();
    }
    // ��ȡһ�����ֵ�����ĸ 
    public static Character getFirstLetter(char ch) { 
         
        byte[] uniCode = null; 
        try { 
            uniCode = String.valueOf(ch).getBytes("GBK"); 
        } catch (UnsupportedEncodingException e) { 
            e.printStackTrace(); 
            return null; 
        } 
        if (uniCode[0] < 128 && uniCode[0] > 0) { // �Ǻ��� 
            return null; 
        } else { 
            return convert(uniCode); 
        } 
    } 
    /**
     * ��ȡһ�����ֵ�ƴ������ĸ�� GB�������ֽڷֱ��ȥ160��ת����10��������ϾͿ��Եõ���λ��
     * ���纺�֡��㡱��GB����0xC4/0xE3���ֱ��ȥ0xA0��160������0x24/0x43
     * 0x24ת��10���ƾ���36��0x43��67����ô������λ�����3667���ڶ��ձ��ж���Ϊ��n��
     */ 
    static char convert(byte[] bytes) { 
        char result = '-'; 
        int secPosValue = 0; 
        int i; 
        for (i = 0; i < bytes.length; i++) { 
            bytes[i] -= GB_SP_DIFF; 
        } 
        secPosValue = bytes[0] * 100 + bytes[1]; 
        for (i = 0; i < 23; i++) { 
            if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) { 
                result = firstLetter[i]; 
                break; 
            } 
        } 
        return result; 
    } 

}


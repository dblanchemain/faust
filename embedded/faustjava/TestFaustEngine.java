import java.io.*;
import com.grame.faust.FaustEngine;
import com.grame.faust.dsp;

class TestFaustEngine
{
	static {
		try {
			String property = System.getProperty("java.library.path");
			System.out.println("java.library.path = " + property);
            System.loadLibrary("FaustEngine"); 
		} catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
		}
    }

    public static void main(String args[]) throws InterruptedException {
		System.out.println("Start Faust Engine");
        
        // noise generator
        String prog1 = "random = +(12345)~*(1103515245); noise = random/2147483647.0; process = noise * vslider(\"Volume\", 0.5, 0, 1, 0.01)<:_,_;";
   
        dsp DSP1 = FaustEngine.create("noise", prog1);
        
        System.out.println(FaustEngine.getJSON(DSP1));
          
        FaustEngine.init(DSP1, "Test");
        FaustEngine.start(DSP1);
        
        System.out.println("getParamsCount : " + FaustEngine.getParamsCount(DSP1));
     
        String prog2  = "import(\"music.lib\"); db2linear1(x) = pow(10.0, x/20.0); smooth(c) = *(1-c) : +~*(c); vol = hslider(\"volume [unit:dB]\", 0, -96, 0, 0.1) : db2linear : smooth(0.999); freq = hslider(\"freq [unit:Hz]\", 1000, 20, 24000, 1); process = vgroup(\"Oscillator\", osc(freq) * vol);";

 
        // oscillator
        dsp DSP2 = FaustEngine.create("osc", prog2);
        
        System.out.println(FaustEngine.getJSON(DSP2));
          
        FaustEngine.init(DSP2, "Test");
        FaustEngine.start(DSP2);
        
        System.out.println("getParamsCount : " + FaustEngine.getParamsCount(DSP2));
    
        Thread.sleep(100*200);
        
        FaustEngine.stop(DSP1);
        FaustEngine.stop(DSP2);
    }
}
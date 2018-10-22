package com.software.abiol.simesapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.software.abiol.simesapp.R;


public class GuestPageActivity extends AppCompatActivity {
    private TextView scrollingText;
    private String guestText;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_page);

        guestText = "<h2>Industrial Maintenance Department</h2><p>Industrial Maintenance Department started as training centre for technical personnel from industries in 1989.  The Industrial Maintenance /centre as it was known then, was established with the assistance of French Government (through its Embassy in Nigeria) to provide training to Engineering/Technical staff from various firms with respect to the maintenance of manufacturing servicing and utility equipment/facilities. The centre started with only the Welding Section which had state-of-art welding equipment and accessories in 1989.  In fact the welding section was named “National Welding Centre” when it was commissioned in January 1989 because it was second to none in Nigeria.<br> <br> By 1991, the Industrial Hydraulics/Pneumatics, Electra-techniques, Industrial Electronics and Metrology sections were added.  All the equipment in these sections was also donated by the French Government.  The scope of training render to technical staff from firms was expanded to cover the above areas.  The duration of the courses run by the centre ranged from one week to two weeks (short courses) and certification of participation were issued to participant at the end of the each course.<br> <br> The National Diploma programme in Industrial Maintenance/Engineering was introduced. While the short courses were administered under the direct supervision of the Director of school of Engineering, the ND Programme was run as a programme in Mechanical engineering department.<br> <br>In July 1995, the Industrial Maintenance Centre metamorphosed into a full-fledged department known as “Industrial Maintenance Engineering Department” and it had a pioneer Head of Department, eight (8) academic staff and ten non-academic staff.  The mandate of the department then was to run the National Diploma (ND) programme in Industrial Maintenance Engineering and then the short courses for technical personnel in industries.  The first set of ND graduates passed out in 1996.<br> <br> The Higher National Diploma programme started in 2003/2004 academic session. The first set of HND graduates passed out in 2006.<br><br>AIMS AND OBJECTIVES<br><br>1.       To produce high level man power in both Electrical and Mechanical systems maintenance.<br> <br>2.      To produce technical personnel in the industries that can face challenges of installing and maintaining the present day’s hi-tech industrial equipment.<br><br>JUSTIFICATION<br><br>Most Industrial Machines are electro-mechanical in nature.  Educational training of highly technical skilled engineers in both electrical and mechanical fields will be of great economic advantage to the industries.<br><br>INTRODUCTION<br><br>Industrial Maintenance Engineering Department trains Technicians and Technologists to acquire sound knowledge in both Mechanical and Electrical Engineering fields.  The department’s graduates have skills in maintenance of various Engineering Systems used in Mechanical, Electrical, Electronics, Pneumatics, Industrial Oil-Hydraulics, Welding, Manufacturing, Construction Equipment etc.</p>";

        webView = findViewById(R.id.guest_web);

        webView.loadData(guestText,"text/html","UTF-8");


    }
}

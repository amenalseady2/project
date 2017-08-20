package com.onemena.app.config;

import com.voler.annotation.ReportEvent;
import com.voler.annotation.ReportParameter;

/**
 * ReportConfig Created by voler on 2017/7/13.
 * 说明：
 */

public class ReportConfig {

    @ReportEvent
    class save_attention {

    }

    @ReportEvent
    class attention_type {
        @ReportParameter
        String type;
    }

    @ReportEvent
    class cancel_attention {
        @ReportParameter
        String type;
    }

    @ReportEvent
    class add_attention {

    }

    @ReportEvent
    class attention_refresh {
        @ReportParameter
        String type;
    }

    @ReportEvent
    class follow_headlines {
        @ReportParameter
        String type;
        @ReportParameter
        String headlines_id;
    }

    @ReportEvent
    class load_button {
    }

    @ReportEvent
    class load_button_over {
        @ReportParameter
        String type;
    }
}

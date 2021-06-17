import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

import colors from 'vuetify/lib/util/colors';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        dark: true,
        themes: {
            light: {
                primary: colors.blue.lighten3,
                secondary: '#6c757d',
                accent: '#3ea2fb',
                error: '#dc3545',
                petrol: '#17a499',
                background: colors.shades.white,
                navBackground: colors.blue.lighten2
            },
            dark: {
                primary: '#8496a0',
                secondary: '#6c757d',
                accent: '#3ea2fb',
                error: '#dc3545',
                petrol: '#17a499',
                background: '#1c2028',
                navBackground: '#1c2028',
                navButton: '#1c2028',
                appTitle: '#2d3139',
            }
        },
        options: {
            customProperties: true
        },
    },
});

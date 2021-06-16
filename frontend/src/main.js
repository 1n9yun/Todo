import Vue from 'vue'
import store from './store'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'

Vue.config.productionTip = false
Vue.config.devtools = true;

new Vue({
    vuetify,
    store,
    router,
    render: h => h(App)
}).$mount('#app')

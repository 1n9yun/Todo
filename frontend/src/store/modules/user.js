export default {
    namespaced: true,

    state: {
        email: "",
        password: "",
    },

    getters: {
        getEmail: state => state.email,
        getPassword: state => state.password,
    },

    mutations: {
        loginSuccess(state, payload) {
            state.email = payload.email;
            state.password = payload.password;
        }
    },

    actions: {
        login({commit}, userInfo) {
            commit("loginSuccess", userInfo);
        }
    }

}
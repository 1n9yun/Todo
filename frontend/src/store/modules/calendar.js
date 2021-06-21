export default {
    namespaced: true,

    state: {
        dates: [],
    },

    getters: {
        getDates: state => state.dates,
    },

    mutations: {
        setDates(state, dates) {
            var sortedDates = dates.slice();
            sortedDates.sort();
            state.dates = sortedDates;
        },
    },

    actions: {
        setDates({commit}, dates) {
            commit('setDates', dates);
        },
    }

}
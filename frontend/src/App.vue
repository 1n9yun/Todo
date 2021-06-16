<template>
	<v-app>
		<v-container fill-height>
			<v-main>
				<Login />
			</v-main>
		</v-container>
	</v-app>
</template>

<script>
import Login from "./components/Login";

export default {
    name: "App",

    components: {
        Login,
    },

    data: () => ({
        //
    }),
};
</script>

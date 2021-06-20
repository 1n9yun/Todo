<template>
	<v-app>
		<v-container
        fill-height
        >
			<v-main>
                <TitleBar
                v-if="this.$store.state.user.email"
                />
				<router-view/>
			</v-main>
		</v-container>
	</v-app>
</template>

<script>
import TitleBar from '@/components/TitleBar'

export default {
    components: {
        TitleBar,
    }
}
</script>


<style>
html, body, .theme--dark.v-application, .theme--light.v-application {
    background: var(--v-background-base) !important;
}
.theme--dark.v-btn.v-btn--has-bg, .theme--light.v-btn.v-btn--has-bg {
    background-color: var(--v-primary-base) !important;
}
.container {
    max-width: 100% !important;
    padding: 0 !important;
    margin: 0 !important;
}
.v-main {
    height: 100%;
}
html, body {
    overflow-y: auto !important;
}

</style>
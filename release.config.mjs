const publishCmd = `
./gradlew publishAllPublicationsToProjectLocalRepository zipMavenCentralPortalPublication releaseMavenCentralPortalPublication || exit 1
`
import config from 'semantic-release-preconfigured-conventional-commits' with { type: "json" };
config.plugins.push(
    [
        "@semantic-release/exec",
        {
            publishCmd,
        },
    ],
    [
        "@semantic-release/github",
        {
            successCommentCondition: false,
            failCommentCondition: false,
        },
    ],
    "@semantic-release/git",
)
export default config

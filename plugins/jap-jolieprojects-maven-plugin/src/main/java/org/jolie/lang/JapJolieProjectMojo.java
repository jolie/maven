package org.jolie.lang;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;


@Mojo( name = "japgen", defaultPhase = LifecyclePhase.PACKAGE )
public class JapJolieProjectMojo
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;
    @Parameter( defaultValue = "${project.basedir}/src/main/jolie", property = "joliePath", required = true )
    private File joliePath;
    @Parameter( defaultValue = "${project.build.finalName}", property = "projectName", required = true )
    private String finalName;
    @Parameter( defaultValue = "${project.groupId}", property = "projectGroupId", required = true )
    private String projectGroupId;
    @Parameter( defaultValue = "${project.name}", property = "projectName", required = true )
    private String projectName;
    @Parameter( defaultValue = "${project.version}", property = "projectVersion", required = true )
    private String projectVersion;

    public void execute()
        throws MojoExecutionException
    {
        if ( !joliePath.exists() ) {
            throw new MojoExecutionException( "File not found in " + joliePath );
        }
        File f = outputDirectory;

        if ( !f.exists() )
        {
            f.mkdirs();
        }

        File outputJap = new File( outputDirectory.toString() + File.separator + finalName + ".jap");
        JapUtils japUtils = new JapUtils(outputJap, projectGroupId, projectName, projectVersion );
        japUtils.createJap(joliePath);

    }
}

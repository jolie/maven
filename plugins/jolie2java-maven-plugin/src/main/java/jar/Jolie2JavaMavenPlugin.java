package jar;


import jolie.CommandLineException;
import jolie.lang.parse.ParserException;
import jolie.lang.parse.SemanticException;
import jolie.lang.parse.ast.Program;
import jolie.lang.parse.util.ParsingUtils;
import jolie.lang.parse.util.ProgramInspector;
import joliex.java.Jolie2Java;
import joliex.java.Jolie2JavaCommandLineParser;
import joliex.java.impl.JavaDocumentCreator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 */
@Mojo( name = "joliegen", defaultPhase = LifecyclePhase.GENERATE_SOURCES )
public class Jolie2JavaMavenPlugin
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}/resources/jolie/main.ol", property = "joliePath", required = true )
    private File joliePath;
    @Parameter( defaultValue = "${project.build.directory}/jolie2java-generated-sources", property = "outputDirectory", required = true )
    private String outputDirectory;
    @Parameter( defaultValue = "org.jolie.jolie2java", property = "PackageName", required = true )
    private String packageName;
    @Parameter( defaultValue = "", property = "TargetPort", required = false )
    private String targetPort;
    @Parameter( defaultValue = "/usr/lib/jolie/include", property = "includePath", required = false )
    private String includePath;

    public void execute()
        throws MojoExecutionException {
        try {
            if ( !joliePath.exists() ) {
                throw new MojoExecutionException( "File not found in " + joliePath );
            }
            System.out.println("Looking for file " + joliePath.getAbsolutePath() );
            String[] args = {  "-i", includePath, joliePath.getAbsolutePath() };
            Jolie2JavaCommandLineParser cmdParser = Jolie2JavaCommandLineParser.create(args, Jolie2Java.class.getClassLoader());

            Program program = ParsingUtils.parseProgram(
                    cmdParser.programStream(),
                    cmdParser.programFilepath().toURI(),
                    cmdParser.charset(),
                    cmdParser.includePaths(),
                    cmdParser.jolieClassLoader(),
                    cmdParser.definedConstants(),
                    false );

            //Program program = parser.parse();
            ProgramInspector inspector = ParsingUtils.createInspector( program );

            String tPort = null;
            if ( targetPort != null && !targetPort.isEmpty() ) {
                tPort = targetPort;
            }
            JavaDocumentCreator instance = new JavaDocumentCreator( inspector, packageName, tPort, false, outputDirectory, false );
            instance.ConvertDocument();
        } catch( IOException e ) {
            e.printStackTrace();
            throw new MojoExecutionException( e.getMessage() );
        } catch( ParserException e ) {
            e.printStackTrace();
            throw new MojoExecutionException( e.getMessage() );
        } catch (CommandLineException e) {
            e.printStackTrace();
            throw new MojoExecutionException( e.getMessage() );
        } catch (SemanticException e) {
            e.printStackTrace();
            throw new MojoExecutionException( e.getMessage() );
        }


    }
}

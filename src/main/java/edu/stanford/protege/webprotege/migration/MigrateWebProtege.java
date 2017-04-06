package edu.stanford.protege.webprotege.migration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import org.apache.commons.cli.*;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2017
 */
public class MigrateWebProtege {


    @Nonnull
    private final Path dataDirectory;

    @Nonnull
    private final String mongoHost;

    private final int mongoPort;

    @Nonnull
    private final String databaseName;

    public MigrateWebProtege(@Nonnull Path dataDirectory,
                             @Nonnull String mongoHost,
                             int mongoPort,
                             @Nonnull String databaseName) {
        this.dataDirectory = dataDirectory;
        this.mongoHost = mongoHost;
        this.mongoPort = mongoPort;
        this.databaseName = databaseName;
    }

    public void run() {
        MongoClient mongoClient = new MongoClient(mongoHost, mongoPort);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        try {
            ProjectDirectoryResolver projectDirectoryResolver = new ProjectDirectoryResolver(dataDirectory);
            MetaProjectResolver metaProjectResolver = new MetaProjectResolver(dataDirectory);
            MetaProject metaProject = new MetaProjectImpl(metaProjectResolver.resolve().toUri());
            ChangeLogFileResolver changeLogFileResolver = new ChangeLogFileResolver(projectDirectoryResolver);
            WebProtegeMigrator migrator = new WebProtegeMigrator(
                    new EntityCrudKitSettingsRenamer(database),
                    new MetaProjectMigrator(metaProject,
                                            new ProjectDetailsConverterFactory(projectDirectoryResolver,
                                                                               changeLogFileResolver),
                                            database),
                    new NotesMigrator(
                            metaProject,
                            changeLogFileResolver,
                            new NotesOntologyDocumentResolver(
                                    projectDirectoryResolver
                            ),
                            database
                    )
            );
            migrator.performMigration();
        } finally {
            mongoClient.close();
        }

    }


    public static void main(String[] args) {

        Options options = new Options();

        Option mongoHostOp = Option.builder("h")
                                   .hasArg()
                                   .desc("Mongo DB host name e.g. localhost or my.server.com" )
                                   .longOpt("mongo-host" )
                                   .required(false)
                                   .build();

        Option mongoPortOp = Option.builder("p")
                                   .hasArg()
                                   .desc("Mongo DB port" )
                                   .longOpt("mongo-port" )
                                   .required(false)
                                   .build();

        Option databaseNameOp = Option.builder("d")
                                      .hasArg()
                                      .desc("WebProtege database name" )
                                      .longOpt("database" )
                                      .required(true)
                                      .build();


        Option dataDirectoryOp = Option.builder("w")
                                       .hasArg()
                                       .desc("The WebProtege data directory path" )
                                       .longOpt("data-directory" )
                                       .required(true)
                                       .build();



        options.addOption(mongoHostOp);
        options.addOption(mongoPortOp);
        options.addOption(databaseNameOp);
        options.addOption(dataDirectoryOp);
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cl = parser.parse(options, args);
            String mongoHost = cl.getOptionValue(mongoHostOp.getOpt(), "localhost");
            int mongoPort = Integer.parseInt(cl.getOptionValue(mongoPortOp.getOpt(), mongoPortOp.getValue("27017")));
            String dbName = cl.getOptionValue(databaseNameOp.getOpt());
            String dataDirectory = cl.getOptionValue(dataDirectoryOp.getOpt());
            Path dataDirectoryPath = Paths.get(dataDirectory);
            MigrateWebProtege migrateWebProtege = new MigrateWebProtege(dataDirectoryPath, mongoHost, mongoPort, dbName);
            migrateWebProtege.run();
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("migrate" , options);
        }


    }
}

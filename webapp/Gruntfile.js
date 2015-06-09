module.exports = function (grunt) {

    grunt.loadNpmTasks('grunt-browserify');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-express-server');
    //grunt.loadNpmTasks('grunt-protractor-runner');
    //grunt.loadNpmTasks('webdriver-manager');
    grunt.loadNpmTasks('grunt-keepalive');
    grunt.loadNpmTasks('grunt-contrib-compress');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-mocha-cli');


    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        browserify: {
            dev: {
                files: {
                    'client/generated/app.js': ['client/js/app.js']
                },
                options: {
                    browserifyOptions: {
                        debug: true
                    },
                    watch: true
                }
            },
            prod: {
                files: {
                    'client/generated/app.js': ['client/js/app.js']
                },
                options: {
                    browserifyOptions: {
                        debug: true // required by minifyify
                    },
                    plugin: [
                        [
                            'remapify',
                            [{
                                src: 'client/js/**/*.js',
                                expose: '',
                                cwd: __dirname
                            }]
                        ],
                        [
                            'minifyify',
                            [{
                                options: {
                                    map: false
                                }
                            }]
                        ]
                    ]
                }
            }
        },

        express: {
            dev: {
                options: {
                    script: './server/server.js'

                }
            }
        },

        less: {
            dev: {
                files: {
                    'client/generated/main.css': ['client/less/main.less']
                }
            },
            prod: {
                files: {
                    'client/generated/main.css': ['client/less/main.less']
                },
                options: {
                    compress: true
                }
            }
        },

        mochacli: {
            all: ['test/*.js']
        },

        run: {
            dev: {
                cmd: 'node server/server.js',
                options: {
                    wait: false,
                    ready: false
                }
            }
        },

        watch: {
            options: {
                atBegin: true
            },
            express: {
                files: 'server/**',
                tasks: ['express:dev'],
                options: {
                    spawn: false,
                    keepAlive: false

                }
            },
            less: {
                files: 'client/less/**/*.less',
                tasks: ['less:dev']
            }
        },

        /*protractor: {
         e2e: {
         options: {
         configFile: "../tests/conf.js",
         // Stops Grunt process if a test fails
         keepAlive: false
         }
         },
         continuous: {
         options: {
         keepAlive: false
         }
         }
         }*/

        compress: {
            main: {
                options: {
                    archive: 'distribution/app.zip'
                },
                files: [
                    {app: ['server/**']},
                    {app: ['client/**']},
                    {app: ['config.json']},
                    {app: ['package.json']}
                ]
            }
        },

        clean: {
            build: ["client/generated/*.*"]
        }

    });
    /*grunt.registerTask('test', function () {
     var config = require("./config.json");
     var request = require('request');
     grunt.task.run('express:dev');
     request.get('http://localhost:' + config.http.port);
     grunt.task.run('e2e-test');
     });*/

    //grunt.registerTask('e2e-test', ['protractor:e2e']);

    grunt.registerTask('test', ['mochacli']);
    grunt.registerTask('develop', ['browserify:dev', 'watch']);
    grunt.registerTask('build', ['clean', 'browserify:prod', 'less:prod', 'compress']);


};

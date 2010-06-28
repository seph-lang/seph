
Seph: LanguageExperiment with(
  goal: (:expressiveness, :concurrency),
  features: [  :dynamic,               :homoiconic,
               :functional,            :proper_tail_calls,
               :prototype_based,       :object_orientation,
               :light_weight_threads,  :parameterized_modules],
  inspirations: set(Ioke, Clojure, Erlang, Newspeak)
),

the_greeting = "Avant, "
Greeter = Something with(
  greet: #("#{the_greeting} #{subject}" println)
)

Seph inspirations select(
  features include?(:object_oriented)
) each(inspiration,
  Greeter with(subject: inspiration) greet
)
